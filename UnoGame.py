def make_groups(n, names, skills, fr):
    m = {names[i]: i for i in range(n)}
    used = [0]*n
    g = []
    for a,b in fr:
        if a in m and b in m:
            i,j = m[a],m[b]
            if not used[i] and not used[j]:
                used[i]=used[j]=1
                g.append(([names[i],names[j]], skills[i]+skills[j]))
    for i in range(n):
        if not used[i]:
            g.append(([names[i]], skills[i]))
    return g

n = int(input().strip())
names = input().split()
skills = list(map(int,input().split()))
f = int(input().strip())
fr = [tuple(input().split()) for _ in range(f)]
r = int(input().strip())
rv = [tuple(input().split()) for _ in range(r)]
lim = int(input().strip())

g = make_groups(n, names, skills, fr)
m = len(g)
pos = {}
for i,(p,_) in enumerate(g):
    for x in p: pos[x]=i

conf = [0]*m
for a,b in rv:
    if a in pos and b in pos:
        x,y = pos[a],pos[b]
        if x!=y:
            conf[x] |= 1<<y
            conf[y] |= 1<<x

sz = [len(g[i][0]) for i in range(m)]
sc = [g[i][1] for i in range(m)]
ordr = list(range(m))
ordr.sort(key=lambda i:(sc[i]/sz[i],sc[i],-sz[i]))

sz = [sz[i] for i in ordr]
sc = [sc[i] for i in ordr]
nc = [0]*m
mp = {old:new for new,old in enumerate(ordr)}
for ni,oi in enumerate(ordr):
    mask=conf[oi]
    nm=0
    for oj in range(m):
        if (mask>>oj)&1: nm|=1<<mp[oj]
    nc[ni]=nm

suf=[0]*(m+1)
for i in range(m-1,-1,-1):
    suf[i]=suf[i+1]+sz[i]

best=0
st=[(0,0,0,0)]
while st:
    i,mask,cost,cnt=st.pop()
    if cost>lim: continue
    if cnt>best: best=cnt
    if i==m: continue
    if cnt+suf[i]<=best: continue
    if (mask&nc[i])==0 and cost+sc[i]<=lim:
        st.append((i+1,mask|(1<<i),cost+sc[i],cnt+sz[i]))
    st.append((i+1,mask,cost,cnt))

print(best,end='')