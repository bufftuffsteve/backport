"""Minimal NBT reader/writer (gzip big-endian), enough for structure templates."""
import gzip, struct, io
END, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, BARR, STR, LIST, COMP, IARR, LARR = range(13)
class Tag:
    __slots__=('t','v','lt')
    def __init__(s,t,v,lt=None): s.t=t; s.v=v; s.lt=lt
    def __repr__(s): return f'Tag({s.t},{s.v!r})'
def _r(f,fmt):
    n=struct.calcsize(fmt); return struct.unpack('>'+fmt,f.read(n))[0]
def _rs(f):
    n=_r(f,'H'); return f.read(n).decode('utf-8')
def _payload(f,t):
    if t==BYTE: return _r(f,'b')
    if t==SHORT: return _r(f,'h')
    if t==INT: return _r(f,'i')
    if t==LONG: return _r(f,'q')
    if t==FLOAT: return _r(f,'f')
    if t==DOUBLE: return _r(f,'d')
    if t==BARR: n=_r(f,'i'); return list(struct.unpack(f'>{n}b',f.read(n)))
    if t==STR: return _rs(f)
    if t==LIST:
        lt=_r(f,'b'); n=_r(f,'i'); return (lt,[Tag(lt,_payload(f,lt)) for _ in range(n)])
    if t==COMP:
        d={}
        while True:
            ct=_r(f,'b')
            if ct==END: return d
            name=_rs(f); d[name]=Tag(ct,_payload(f,ct))
    if t==IARR: n=_r(f,'i'); return list(struct.unpack(f'>{n}i',f.read(4*n)))
    if t==LARR: n=_r(f,'i'); return list(struct.unpack(f'>{n}q',f.read(8*n)))
    raise ValueError(t)
def load(path):
    f=io.BytesIO(gzip.open(path).read()); t=_r(f,'b'); name=_rs(f); return name, Tag(t,_payload(f,t))
def _w(f,fmt,v): f.write(struct.pack('>'+fmt,v))
def _ws(f,s): b=s.encode('utf-8'); _w(f,'H',len(b)); f.write(b)
def _wp(f,tag):
    t,v=tag.t,tag.v
    if t==BYTE: _w(f,'b',v)
    elif t==SHORT: _w(f,'h',v)
    elif t==INT: _w(f,'i',v)
    elif t==LONG: _w(f,'q',v)
    elif t==FLOAT: _w(f,'f',v)
    elif t==DOUBLE: _w(f,'d',v)
    elif t==BARR: _w(f,'i',len(v)); f.write(struct.pack(f'>{len(v)}b',*v))
    elif t==STR: _ws(f,v)
    elif t==LIST:
        lt,items=v; _w(f,'b',lt if items else (lt or END)); _w(f,'i',len(items))
        for i in items: _wp(f,i)
    elif t==COMP:
        for k,c in v.items(): _w(f,'b',c.t); _ws(f,k); _wp(f,c)
        _w(f,'b',END)
    elif t==IARR: _w(f,'i',len(v)); f.write(struct.pack(f'>{len(v)}i',*v))
    elif t==LARR: _w(f,'i',len(v)); f.write(struct.pack(f'>{len(v)}q',*v))
def save(path,name,tag):
    f=io.BytesIO(); _w(f,'b',tag.t); _ws(f,name); _wp(f,tag)
    with gzip.GzipFile(path,'wb',mtime=0) as g: g.write(f.getvalue())
def plain(tag):
    """Tag tree -> python objects (for reading)."""
    if tag.t==COMP: return {k:plain(v) for k,v in tag.v.items()}
    if tag.t==LIST: return [plain(i) for i in tag.v[1]]
    return tag.v
