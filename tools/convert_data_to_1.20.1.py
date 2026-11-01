"""
Converts this mod's 1.21.1 resources to 1.20.1's formats, in place.

    python tools/convert_data_to_1.20.1.py common/src/main/resources common/src/generated/resources

Use it after copying data from the 1.21.1 branch (multiloader-1.21.1). It is safe to run twice.
What it changes:
  - folder names 1.21 renamed (recipe -> recipes, loot_table -> loot_tables, tags/block -> tags/blocks, ...)
  - recipe results ({"id"} -> {"item"} / plain strings), and drops recipes whose result needs item components
  - item, tool and enchantment predicates in loot tables and advancements
  - map loot: decoration names, item names, biome checks; drops maps to structures 1.20.1 doesn't have
  - worldgen number ranges ("value" wrapper), and vanilla features 1.20.1 doesn't have yet
  - "short_grass" back to its 1.20.1 name "grass" (JSON and structure files)
  - structure files' data version
  - removes NeoForge-only data maps, and recipe-book advancements for recipes it dropped
"""
import json, os, shutil, sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import nbt  # noqa: E402

DATA_VERSION_1_20_1 = 3465
FOLDER_RENAMES = {
    'recipe': 'recipes', 'loot_table': 'loot_tables', 'advancement': 'advancements', 'structure': 'structures',
    'tags/block': 'tags/blocks', 'tags/item': 'tags/items', 'tags/entity_type': 'tags/entity_types',
    'tags/fluid': 'tags/fluids', 'tags/game_event': 'tags/game_events',
}
# Things 1.20.1 doesn't have. Map loot pointing at these is removed, as are tags made only for them.
MISSING_STRUCTURE_TAGS = {'minecraft:on_buried_trial_chambers_maps'}
RENAMED_IDS = {'minecraft:short_grass': 'minecraft:grass'}
# Map icons 1.20.1 has; others (village, jungle temple, swamp hut icons came later) become the red X.
MAP_ICONS_1_20_1 = {'player', 'frame', 'red_marker', 'blue_marker', 'target_x', 'target_point', 'player_off_map',
                    'player_off_limits', 'mansion', 'monument', 'red_x'}
# Vanilla worldgen features that don't exist yet in 1.20.1; biomes just leave them out.
MISSING_PLACED_FEATURES = {'minecraft:ore_diamond_medium'}
# Number ranges that 1.20.1 wraps in a "value" object in worldgen files (height ranges aren't).
WRAPPED_NUMBER_PROVIDERS = {'minecraft:uniform', 'minecraft:biased_to_bottom', 'minecraft:clamped',
                            'minecraft:clamped_normal', 'minecraft:trapezoid'}


def convert_worldgen(obj):
    if isinstance(obj, list):
        return [convert_worldgen(v) for v in obj if not (isinstance(v, str) and v in MISSING_PLACED_FEATURES)]
    if not isinstance(obj, dict):
        return obj
    obj = {k: convert_worldgen(v) for k, v in obj.items()}
    numbers = [v for k, v in obj.items() if k not in ('type', 'source')]
    if (obj.get('type') in WRAPPED_NUMBER_PROVIDERS and 'value' not in obj and numbers
            and all(isinstance(v, (int, float)) for v in numbers)):
        source = obj.pop('source', None)
        value = {k: v for k, v in obj.items() if k != 'type'}
        if source is not None:
            value['source'] = source
        obj = {'type': obj['type'], 'value': value}
    return obj


def rename_ids(obj):
    if isinstance(obj, dict):
        return {k: rename_ids(v) for k, v in obj.items()}
    if isinstance(obj, list):
        return [rename_ids(v) for v in obj]
    if isinstance(obj, str):
        return RENAMED_IDS.get(obj, obj)
    return obj


def item_predicate(p):
    """1.21 {"items": "x" | "#tag" | [...], "predicates": {...}} -> 1.20.1 {"items": [...]} / {"tag": ...}."""
    out = {k: v for k, v in p.items() if k not in ('items', 'predicates')}
    items = p.get('items')
    if isinstance(items, str):
        if items.startswith('#'):
            out['tag'] = items[1:]
        else:
            out['items'] = [items]
    elif isinstance(items, list):
        out['items'] = items
    enchants = p.get('predicates', {}).get('minecraft:enchantments')
    if enchants:
        out['enchantments'] = [{'enchantment': e['enchantments'], **{k: v for k, v in e.items() if k != 'enchantments'}}
                               for e in enchants]
    return out


def convert_loot(obj):
    if isinstance(obj, list):
        return [convert_loot(v) for v in obj if not (isinstance(v, dict) and maps_to_missing_structure(v))]
    if not isinstance(obj, dict):
        return obj
    obj = {k: convert_loot(v) for k, v in obj.items()}
    if obj.get('condition') == 'minecraft:match_tool' and 'predicate' in obj:
        obj['predicate'] = item_predicate(obj['predicate'])
    if obj.get('condition') == 'minecraft:location_check':
        pred = obj.get('predicate', {})
        if isinstance(pred.get('biomes'), str):
            pred['biome'] = pred.pop('biomes')
    if obj.get('function') == 'minecraft:exploration_map' and 'decoration' in obj:
        icon = obj['decoration'].split(':', 1)[-1]
        obj['decoration'] = icon if icon in MAP_ICONS_1_20_1 or icon.startswith('banner_') else 'red_x'

    if obj.get('function') == 'minecraft:set_name' and obj.get('target') == 'item_name':
        del obj['target']
        if isinstance(obj.get('name'), dict):
            obj['name'] = {**obj['name'], 'italic': False}  # item names weren't italic; custom names are
    return obj


def maps_to_missing_structure(entry):
    return any(f.get('function') == 'minecraft:exploration_map' and f.get('destination') in MISSING_STRUCTURE_TAGS
               for f in entry.get('functions', []))


def convert_recipe(r):
    """Returns the 1.20.1 recipe, or None if 1.20.1 can't express it."""
    res = r.get('result')
    if isinstance(res, dict) and 'id' in res:
        if res.get('components'):
            return None
        if r['type'] in ('minecraft:stonecutting',):
            r['result'] = res['id']
            r['count'] = res.get('count', 1)
        elif r['type'] in ('minecraft:smelting', 'minecraft:blasting', 'minecraft:smoking', 'minecraft:campfire_cooking'):
            r['result'] = res['id']
        else:
            r['result'] = {'item': res['id'], **({'count': res['count']} if res.get('count', 1) != 1 else {})}
    return r


def convert_advancement(a):
    for c in a.get('criteria', {}).values():
        if c.get('trigger') == 'minecraft:inventory_changed':
            cond = c.get('conditions', {})
            if 'items' in cond:
                cond['items'] = [item_predicate(p) for p in cond['items']]
    return a


def rename_folders(root):
    for ns in os.listdir(os.path.join(root, 'data')) if os.path.isdir(os.path.join(root, 'data')) else []:
        base = os.path.join(root, 'data', ns)
        for old, new in FOLDER_RENAMES.items():
            src, dst = os.path.join(base, old), os.path.join(base, new)
            if os.path.isdir(src):
                if os.path.isdir(dst):
                    shutil.copytree(src, dst, dirs_exist_ok=True)
                    shutil.rmtree(src)
                else:
                    os.makedirs(os.path.dirname(dst), exist_ok=True)
                    shutil.move(src, dst)


def category(root, dirpath):
    """The data folder a file is in, e.g. "recipes" for data/minecraft/recipes/x.json."""
    parts = os.path.relpath(dirpath, os.path.join(root, 'data')).replace('\\', '/').split('/')
    return parts[1] if len(parts) > 1 else ''


def rewrite_json(path, func):
    with open(path) as f:
        data = json.load(f)
    new = func(data)
    if new is None:
        os.remove(path)
        print('removed', path)
        return
    with open(path, 'w') as f:
        json.dump(new, f, indent=2)
        f.write('\n')


def convert_structure(path):
    name, tag = nbt.load(path)
    root = tag.v
    root['DataVersion'] = nbt.Tag(nbt.INT, DATA_VERSION_1_20_1)
    for state in root['palette'].v[1]:
        block = state.v['Name']
        block.v = RENAMED_IDS.get(block.v, block.v)
    nbt.save(path, name, tag)


def convert(root):
    neo = os.path.join(root, 'data', 'neoforge')
    if os.path.isdir(neo):
        shutil.rmtree(neo)
    rename_folders(root)
    for dirpath, _, files in os.walk(os.path.join(root, 'data')):
        rel = dirpath.replace('\\', '/')
        for name in files:
            path = os.path.join(dirpath, name)
            if name.endswith('.nbt'):
                convert_structure(path)
                continue
            if not name.endswith('.json'):
                continue
            if '/tags/worldgen/structure' in rel and f"minecraft:{name[:-5]}" in MISSING_STRUCTURE_TAGS:
                os.remove(path)
                print('removed', path)
                continue
            rewrite_json(path, rename_ids)
            kind = category(root, dirpath)
            if kind == 'recipes':
                rewrite_json(path, convert_recipe)
            elif kind == 'loot_tables':
                rewrite_json(path, convert_loot)
            elif kind == 'advancements':
                rewrite_json(path, convert_advancement)
            elif kind == 'worldgen':
                rewrite_json(path, convert_worldgen)


def remove_orphan_recipe_advancements(roots):
    """Recipe-book advancements whose recipes were all removed above."""
    recipes = set()
    for root in roots:
        for dirpath, _, files in os.walk(os.path.join(root, 'data')):
            if category(root, dirpath) == 'recipes':
                ns = os.path.relpath(dirpath, os.path.join(root, 'data')).split(os.sep)[0]
                recipes.update(f"{ns}:{n[:-5]}" for n in files if n.endswith('.json'))
    for root in roots:
        for dirpath, _, files in os.walk(os.path.join(root, 'data')):
            if category(root, dirpath) != 'advancements':
                continue
            for name in files:
                path = os.path.join(dirpath, name)
                with open(path) as f:
                    wanted = json.load(f).get('rewards', {}).get('recipes', [])
                if wanted and not any(r in recipes for r in wanted):
                    os.remove(path)
                    print('removed', path)


if __name__ == '__main__':
    for root in sys.argv[1:]:
        convert(root)
        print('converted', root)
    remove_orphan_recipe_advancements(sys.argv[1:])
