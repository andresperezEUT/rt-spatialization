// C equations for extended source 3rd order ambisonics encoding

/*

////////////////////////////////////////////////////////
general wide source:


[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p]
w= 1,

y = (Sqrt(3)*de*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da/2.))/(2.*da) +
       (Sqrt(3)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da/2.)*Sin(de))/(2.*da),

    z=   (Sqrt(3)*Csc(de/2.)*Sin(de)*Sin(e))/2.,

     x=  (Sqrt(3)*de*Cos(a)*Csc(de/2.)*Sec(e)*Sin(da/2.))/(2.*da) +
       (Sqrt(3)*Cos(a)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(da/2.)*Sin(de))/(2.*da),

     v=  (3*Sqrt(15)*Cos(a)*Sin(a)*Sin(da))/(4.*da) +
       (Sqrt(1.6666666666666667)*Cos(a)*Cos(3*e)*Sec(e)*Sin(a)*Sin(da))/(4.*da) +
       (Sqrt(1.6666666666666667)*Cos(a)*Cos(de)*Cos(3*e)*Sec(e)*Sin(a)*Sin(da))/
        (2.*da),

t = (Sqrt(1.6666666666666667)*Cos((de - 2*e)/2.)**3*Csc(de/2.)*Sec(e)*
          Sin(a)*Sin(da/2.))/da -
       (Sqrt(1.6666666666666667)*Cos(de/2. + e)**3*Csc(de/2.)*Sec(e)*Sin(a)*
          Sin(da/2.))/da,

r = Sqrt(5)/4. - (Sqrt(5)*Cos(e)**2)/2. -
       (Sqrt(5)*Cos(de)*Cos(3*e)*Sec(e))/4.,

    s=  (Sqrt(1.6666666666666667)*Cos(a)*Cos((de - 2*e)/2.)**3*Csc(de/2.)*Sec(e)*
          Sin(da/2.))/da - (Sqrt(1.6666666666666667)*Cos(a)*Cos(de/2. + e)**3*
          Csc(de/2.)*Sec(e)*Sin(da/2.))/da,

     u = (3*Sqrt(15)*Cos(2*a)*Sin(da))/(8.*da) +
       (Sqrt(1.6666666666666667)*Cos(2*a)*Cos(3*e)*Sec(e)*Sin(da))/(8.*da) +
       (Sqrt(1.6666666666666667)*Cos(2*a)*Cos(de)*Cos(3*e)*Sec(e)*Sin(da))/(4.*da),

   q =    (Sqrt(17.5)*de*Csc(de/2.)*Sec(e)*Sin(3*a)*Sin((3*da)/2.))/(16.*da) +
       (Sqrt(17.5)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(3*a)*Sin((3*da)/2.)*Sin(de))/
        (12.*da) + (Sqrt(17.5)*Cos(4*e)*Csc(de/2.)*Sec(e)*Sin(3*a)*Sin((3*da)/2.)*
          Sin(2*de))/(96.*da),

o = (Sqrt(105)*Cos(a)*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da)*
          Sin(de)*Sin(2*e))/(8.*da) +
       (Sqrt(105)*Cos(a)*Cos(de)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da)*Sin(de)*
         Sin(2*e))/(8.*da),

m = (Sqrt(10.5)*de*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da/2.))/
        (16.*da) - (Sqrt(10.5)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(a)*Sin(da/2.)*
          Sin(de))/(4.*da) - (5*Sqrt(10.5)*Cos(4*e)*Csc(de/2.)*Sec(e)*Sin(a)*
          Sin(da/2.)*Sin(2*de))/(32.*da),

  k =     -(Sqrt(7)*Csc(de/2.)*Sec(e)*Sin(de)*Sin(2*e))/16. -
       (5*Sqrt(7)*Cos(de)*Csc(de/2.)*Sec(e)*Sin(de)*Sin(4*e))/32.,

    l =  (Sqrt(10.5)*de*Cos(a)*Csc(de/2.)*Sec(e)*Sin(da/2.))/(16.*da) -
       (Sqrt(10.5)*Cos(a)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(da/2.)*Sin(de))/(4.*da) -
       (5*Sqrt(10.5)*Cos(a)*Cos(4*e)*Csc(de/2.)*Sec(e)*Sin(da/2.)*Sin(2*de))/
        (32.*da),

n = (Sqrt(105)*Cos(2*a)*Csc(de/2.)*Sec(e)*Sin(da)*Sin(de)*Sin(2*e))/
        (16.*da) + (Sqrt(105)*Cos(2*a)*Cos(de)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin(da)*
          Sin(de)*Sin(2*e))/(16.*da),

   p =    (Sqrt(17.5)*de*Cos(3*a)*Csc(de/2.)*Sec(e)*Sin((3*da)/2.))/(16.*da) +
       (Sqrt(17.5)*Cos(3*a)*Cos(2*e)*Csc(de/2.)*Sec(e)*Sin((3*da)/2.)*Sin(de))/
        (12.*da) + (Sqrt(17.5)*Cos(3*a)*Cos(4*e)*Csc(de/2.)*Sec(e)*Sin((3*da)/2.)*
          Sin(2*de))/(96.*da)


////////////////////////////////////////////////////////

ring source:

[w, y, z, x, v, t, r, s, u, q, o, m, k, l, n, p]
w=1,
y=0,
z=Sqrt(3)*Sin(e),
x=0,
v=0,
t=0,
r=(Sqrt(5)*(-1 + 3*Power(Sin(e),2)))/2.,
s=0,
u=0,
q=0,
o=0,
m=0,
k=   -(Sqrt(7)*(1 + 5*Cos(2*e))*Sin(e))/4.,
l=0,
n=0,
p=0

////////////////////////////////////////////////////////

meridian source

w=1,
y=0,
z=0,
x=0,
v=(Sqrt(15)*Cos(phi)*Sin(phi))/2.,
t=0,
r=Sqrt(5)/4.,
s=0,
u=(Sqrt(15)*Cos(2*phi))/4.,
q=0,
o=0,
m=0,
k=0,
l=0,
n=0,
p=0


////////////////////////////////////////////////////////

semimeridian source


w=1,
y=(2*Sqrt(3)*Sin(phi))/Pi,
z=0,
x=(2*Sqrt(3)*Cos(phi))/Pi,
v=(Sqrt(15)*Cos(phi)*Sin(phi))/2.,
t=0,
r=Sqrt(5)/4.,
s=0,
u=(Sqrt(15)*Cos(2*phi))/4.,
q=(Sqrt(70)*Sin(3*phi))/(3.*Pi),
o=0,
m=(Sqrt(4.666666666666667)*Sin(phi))/Pi,
k=0,
l=(Sqrt(4.666666666666667)*Cos(phi))/Pi,
n=0,
p=(Sqrt(70)*Cos(3*phi))/(3.*Pi)


*/
