package com.longriver.netpro.common.sohu;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PepoleScript {
	
	public static void main(String args[]){
		SohuScript s = new SohuScript();
		try {
			System.out.println(s.getHexMd5(s.getHexMd5(s.getHexMd5("wangmeng121")+"1FNGL2")+"wkDfBK"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getHexMd5(String pwd) throws Exception{
		ScriptEngineManager sem = new ScriptEngineManager();
		ScriptEngine se = sem.getEngineByName("JavaScript");
		Object result = se.eval("var hexcase=0,chrsz=8,MIN_HTTS_TIMESTAMP=1293156753137;\t\n"	
						+ "function hex_md5(e){return binl2hex(core_md5(str2binl(e),e.length*chrsz))}	\t\n"	
						+ "function core_md5(e,t){e[t>>5]|=128<<t%32,e[(t+64>>>9<<4)+14]=t;for(var n=1732584193,i=-271733879,o=-1732584194,r=271733878,a=0;e.length>a;a+=16){var s=n,l=i,c=o,u=r;n=md5_ff(n,i,o,r,e[a+0],7,-680876936),r=md5_ff(r,n,i,o,e[a+1],12,-389564586),o=md5_ff(o,r,n,i,e[a+2],17,606105819),i=md5_ff(i,o,r,n,e[a+3],22,-1044525330),n=md5_ff(n,i,o,r,e[a+4],7,-176418897),r=md5_ff(r,n,i,o,e[a+5],12,1200080426),o=md5_ff(o,r,n,i,e[a+6],17,-1473231341),i=md5_ff(i,o,r,n,e[a+7],22,-45705983),n=md5_ff(n,i,o,r,e[a+8],7,1770035416),r=md5_ff(r,n,i,o,e[a+9],12,-1958414417),o=md5_ff(o,r,n,i,e[a+10],17,-42063),i=md5_ff(i,o,r,n,e[a+11],22,-1990404162),n=md5_ff(n,i,o,r,e[a+12],7,1804603682),r=md5_ff(r,n,i,o,e[a+13],12,-40341101),o=md5_ff(o,r,n,i,e[a+14],17,-1502002290),i=md5_ff(i,o,r,n,e[a+15],22,1236535329),n=md5_gg(n,i,o,r,e[a+1],5,-165796510),r=md5_gg(r,n,i,o,e[a+6],9,-1069501632),o=md5_gg(o,r,n,i,e[a+11],14,643717713),i=md5_gg(i,o,r,n,e[a+0],20,-373897302),n=md5_gg(n,i,o,r,e[a+5],5,-701558691),r=md5_gg(r,n,i,o,e[a+10],9,38016083),o=md5_gg(o,r,n,i,e[a+15],14,-660478335),i=md5_gg(i,o,r,n,e[a+4],20,-405537848),n=md5_gg(n,i,o,r,e[a+9],5,568446438),r=md5_gg(r,n,i,o,e[a+14],9,-1019803690),o=md5_gg(o,r,n,i,e[a+3],14,-187363961),i=md5_gg(i,o,r,n,e[a+8],20,1163531501),n=md5_gg(n,i,o,r,e[a+13],5,-1444681467),r=md5_gg(r,n,i,o,e[a+2],9,-51403784),o=md5_gg(o,r,n,i,e[a+7],14,1735328473),i=md5_gg(i,o,r,n,e[a+12],20,-1926607734),n=md5_hh(n,i,o,r,e[a+5],4,-378558),r=md5_hh(r,n,i,o,e[a+8],11,-2022574463),o=md5_hh(o,r,n,i,e[a+11],16,1839030562),i=md5_hh(i,o,r,n,e[a+14],23,-35309556),n=md5_hh(n,i,o,r,e[a+1],4,-1530992060),r=md5_hh(r,n,i,o,e[a+4],11,1272893353),o=md5_hh(o,r,n,i,e[a+7],16,-155497632),i=md5_hh(i,o,r,n,e[a+10],23,-1094730640),n=md5_hh(n,i,o,r,e[a+13],4,681279174),r=md5_hh(r,n,i,o,e[a+0],11,-358537222),o=md5_hh(o,r,n,i,e[a+3],16,-722521979),i=md5_hh(i,o,r,n,e[a+6],23,76029189),n=md5_hh(n,i,o,r,e[a+9],4,-640364487),r=md5_hh(r,n,i,o,e[a+12],11,-421815835),o=md5_hh(o,r,n,i,e[a+15],16,530742520),i=md5_hh(i,o,r,n,e[a+2],23,-995338651),n=md5_ii(n,i,o,r,e[a+0],6,-198630844),r=md5_ii(r,n,i,o,e[a+7],10,1126891415),o=md5_ii(o,r,n,i,e[a+14],15,-1416354905),i=md5_ii(i,o,r,n,e[a+5],21,-57434055),n=md5_ii(n,i,o,r,e[a+12],6,1700485571),r=md5_ii(r,n,i,o,e[a+3],10,-1894986606),o=md5_ii(o,r,n,i,e[a+10],15,-1051523),i=md5_ii(i,o,r,n,e[a+1],21,-2054922799),n=md5_ii(n,i,o,r,e[a+8],6,1873313359),r=md5_ii(r,n,i,o,e[a+15],10,-30611744),o=md5_ii(o,r,n,i,e[a+6],15,-1560198380),i=md5_ii(i,o,r,n,e[a+13],21,1309151649),n=md5_ii(n,i,o,r,e[a+4],6,-145523070),r=md5_ii(r,n,i,o,e[a+11],10,-1120210379),o=md5_ii(o,r,n,i,e[a+2],15,718787259),i=md5_ii(i,o,r,n,e[a+9],21,-343485551),n=safe_add(n,s),i=safe_add(i,l),o=safe_add(o,c),r=safe_add(r,u)}return[n,i,o,r]}	\t\n"	
						+ "function md5_cmn(e,t,n,i,o,r){return safe_add(bit_rol(safe_add(safe_add(t,e),safe_add(i,r)),o),n)}	\t\n"	
						+ "function md5_ff(e,t,n,i,o,r,a){return md5_cmn(t&n|~t&i,e,t,o,r,a)}	\t\n"	
						+ "function md5_gg(e,t,n,i,o,r,a){return md5_cmn(t&i|n&~i,e,t,o,r,a)}	\t\n"	
						+ "function md5_hh(e,t,n,i,o,r,a){return md5_cmn(t^n^i,e,t,o,r,a)}	\t\n"	
						+ "function md5_ii(e,t,n,i,o,r,a){return md5_cmn(n^(t|~i),e,t,o,r,a)}	\t\n"	
						+ "function safe_add(e,t){var n=(65535&e)+(65535&t),i=(e>>16)+(t>>16)+(n>>16);return i<<16|65535&n}	\t\n"	
						+ "function bit_rol(e,t){return e<<t|e>>>32-t}	\t\n"	
						+ "function binl2hex(e){for(var t=hexcase?\"0123456789ABCDEF\":\"0123456789abcdef\",n=\"\",i=0;4*e.length>i;i++)n+=t.charAt(15&e[i>>2]>>8*(i%4)+4)+t.charAt(15&e[i>>2]>>8*(i%4));return n}	\t\n"	
						+ "function str2binl(e){for(var t=[],n=(1<<chrsz)-1,i=0;e.length*chrsz>i;i+=chrsz)t[i>>5]|=(e.charCodeAt(i/chrsz)&n)<<i%32;return t}	\t\n"	
						+ "function b64_423(e){for(var t=[\"A\",\"B\",\"C\",\"D\",\"E\",\"F\",\"G\",\"H\",\"I\",\"J\",\"K\",\"L\",\"M\",\"N\",\"O\",\"P\",\"Q\",\"R\",\"S\",\"T\",\"U\",\"V\",\"W\",\"X\",\"Y\",\"Z\",\"a\",\"b\",\"c\",\"d\",\"e\",\"f\",\"g\",\"h\",\"i\",\"j\",\"k\",\"l\",\"m\",\"n\",\"o\",\"p\",\"q\",\"r\",\"s\",\"t\",\"u\",\"v\",\"w\",\"x\",\"y\",\"z\",\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\",\"-\",\"_\"],n=new String,i=0;e.length>i;i++){for(var o=0;64>o;o++)if(e.charAt(i)==t[o]){var r=o.toString(2);n+=(\"000000\"+r).substr(r.length);break}if(64==o)return 2==i?n.substr(0,8):n.substr(0,16)}return n}	\t\n"	
						+ "function b2i(e){for(var t=0,n=128,i=0;8>i;i++,n/=2)\"1\"==e.charAt(i)&&(t+=n);return String.fromCharCode(t)}	\t\n"	
						+ "function b64_decodex(e){var t,n=[],i=\"\";for(t=0;e.length>t;t+=4)i+=b64_423(e.substr(t,4));for(t=0;i.length>t;t+=8)n+=b2i(i.substr(t,8));return n}	\t\n"	
						+ "function utf8to16(e){var t,n,i,o,r,a,s,l,c;for(t=[],o=e.length,n=i=0;o>n;){switch(r=e.charCodeAt(n++),r>>4){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:t[i++]=e.charAt(n-1);break;case 12:case 13:a=e.charCodeAt(n++),t[i++]=String.fromCharCode((31&r)<<6|63&a);break;case 14:a=e.charCodeAt(n++),s=e.charCodeAt(n++),t[i++]=String.fromCharCode((15&r)<<12|(63&a)<<6|63&s);break;case 15:switch(15&r){case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:a=e.charCodeAt(n++),s=e.charCodeAt(n++),l=e.charCodeAt(n++),c=(7&r)<<18|(63&a)<<12|(63&s)<<6|(63&l)-65536,t[i]=c>=0&&1048575>=c?String.fromCharCode(55296|1023&c>>>10,56320|1023&c):\"?\";break;case 8:case 9:case 10:case 11:n+=4,t[i]=\"?\";break;case 12:case 13:n+=5,t[i]=\"?\"}}i++}return t.join(\"\")}	\t\n"	
						+ "function getStringLen(e){var t=e.match(/[^\\x00-\\xff]/gi);return e.length+(null==t?0:t.length)}");
		result = ((Invocable)se).invokeFunction("hex_md5", pwd);
		return (String) result;
	}
	
}
