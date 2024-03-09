package com.mikepenz.hypnoticcanvas.shaders

object PurpleLiquid : Shader {
    override val name: String
        get() = "PurpleLiquid"

    override val authorName: String
        get() = "fouad"

    override val authorUrl: String
        get() = "https://www.shadertoy.com/user/fouad"

    override val credit: String
        get() = "https://www.shadertoy.com/view/dsXyzf"

    override val license: String
        get() = "Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License"

    override val licenseUrl: String
        get() = "https://www.shadertoy.com/terms"

    override val sksl = """
uniform float uTime;
uniform vec3 uResolution;



const float arrow_density = 4.5;
const float arrow_length = .45;

const int iterationTime1 = 20;
const int iterationTime2 = 20;
const int vector_field_mode = 0;
const float scale = 6.;

const float velocity_x = 0.1;
const float velocity_y = 0.2;

const float mode_2_speed = 2.5;
const float mode_1_detail = 200.;
const float mode_1_twist = 50.;

const bool isArraw = true;

const vec3 luma = vec3(0.2126, 0.7152, 0.0722);


float f(in vec2 p)
{
    return sin(p.x+sin(p.y+uTime*velocity_x)) * sin(p.y*p.x*0.1+uTime*velocity_y);
}


struct Field {
    vec2 vel;
    vec2 pos;
};

//---------------Field to visualize defined here-----------------

Field field(in vec2 p,in int mode)
{
    Field field;
    if(mode == 0){
    	vec2 ep = vec2(0.05,0.);
        vec2 rz= vec2(0);
        //# centered grid sampling
        for( int i=0; i<iterationTime1; i++ )
        {
            float t0 = f(p);
            float t1 = f(p + ep.xy);
            float t2 = f(p + ep.yx);
            vec2 g = vec2((t1-t0), (t2-t0))/ep.xx;
            vec2 t = vec2(-g.y,g.x);
            
            //# need update 'p' for next iteration,but give it some change.
            p += (mode_1_twist*0.01)*t + g*(1./mode_1_detail);
            p.x = p.x + sin( uTime*mode_2_speed/10.)/10.;
            p.y = p.y + cos(uTime*mode_2_speed/10.)/10.;
            rz= g; 
        }
        field.vel = rz;
        return field;
    }
    
    if(mode == 1){
        vec2 ep = vec2(0.05,0.);
        vec2 rz= vec2(0);
        //# centered grid sampling
        for( int i=0; i<iterationTime1; i++ )
        {
            float t0 = f(p);
            float t1 = f(p + ep.xy);
            float t2 = f(p + ep.yx);
            vec2 g = vec2((t1-t0), (t2-t0))/ep.xx;
            vec2 t = vec2(-g.y,g.x);

            //# need update 'p' for next iteration,but give it some change.
            p += (mode_1_twist*0.01)*t + g*(1./mode_1_detail);
            p.x = p.x + sin( uTime*mode_2_speed/10.)/10.;
            p.y = p.y + cos(uTime*mode_2_speed/10.)/10.;
            rz= g;
        }
        
        field.vel = rz;
        // add curved effect into curved mesh
        for(int i=1; i<iterationTime2; i++){
            //# try comment these 2 lines,will give more edge effect
            p.x+=0.3/float(i)*sin(float(i)*3.*p.y+uTime*mode_2_speed) + 0.5;
            p.y+=0.3/float(i)*cos(float(i)*3.*p.x + uTime*mode_2_speed) + 0.5;
        }
        field.pos = p;
        return field;
    }
    
    return field;
}
//---------------------------------------------------------------

float segm(in vec2 p, in vec2 a, in vec2 b) //from iq
{
	vec2 pa = p - a;
	vec2 ba = b - a;
	float h = clamp(dot(pa,ba)/dot(ba,ba), 0., 1.);
	return length(pa - ba*h)*20.*arrow_density;
}

float fieldviz(in vec2 p,in int mode)
{
    vec2 ip = floor(p*arrow_density)/arrow_density + .5/arrow_density;   
    vec2 t = field(ip,mode).vel;
    float m = min(0.1,pow(length(t),0.5)*(arrow_length/arrow_density));
    vec2 b = normalize(t)*m;
    float rz = segm(p, ip, ip+b);
    vec2 prp = (vec2(-b.y,b.x));
    rz = min(rz,segm(p, ip+b, ip+b*0.65+prp*0.3));
    return clamp(min(rz,segm(p, ip+b, ip+b*0.65-prp*0.3)),0.,1.);
}


vec3 getRGB(in Field fld,in int mode){

    if(mode == 0){
        vec2 p = fld.vel;
        vec3 origCol = vec3(p * 0.5 + 0.5, 1.5);
        return origCol;
    }
    
    if(mode == 1){
        vec2 p = fld.pos;
        float r=cos(p.x+p.y+1.)*.5+.5;
        float g=sin(p.x+p.y+1.)*.5+.5;
        float b=(sin(p.x+p.y)+cos(p.x+p.y))*.3+.5;
        vec3 col = sin(vec3(-.3,0.1,0.5)+p.x-p.y)*0.65+0.35;
        return vec3(r,g,b);
    }

    return vec3(0, 0, 0);
}

vec4 main( vec2 fragCoord )
{
	vec2 p = fragCoord.xy / uResolution.xy-0.5 ;
	p.x *= uResolution.x/uResolution.y;
    p *= scale;
    
    vec2 uv = fragCoord.xy / uResolution.xy;
    vec3 col;
    float fviz;
    
    int vector_mode = 0;
    Field fld = field(p,vector_mode);
    col = getRGB(fld,vector_mode) * 0.85;    
	return vec4(col,1.0);
}
    """
}