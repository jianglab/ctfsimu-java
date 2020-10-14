<!--
function CheckBrowser() {	
	if(navigator.platform.indexOf("Mac") != -1 ){
		/*alert("You are using a Mac computer, most likely my Java applet would not run because of "
			+ "bad Java support in Mac OS. Please try it on a Windows, Linux or SGI computer\n"
		);*/
	}
	else if(navigator.appName.indexOf("Microsoft") !=-1){
		var start=navigator.appVersion.indexOf("MSIE ")+5;
		var verstr=navigator.appVersion.substr(start);
		var ver=parseFloat(verstr);
		if(ver<5.5){
			alert("You are using Microsoft Internet Explorer version "
				+ ver +", please update to at least version 5.5 to get "
				+ "full support of this page !\n"
			);
		}
	}
	else if(navigator.appName.indexOf("Netscape") != -1 ){
		var ver=parseFloat(navigator.appVersion);
		if(ver<5){
			alert("You are using Netscape version "+ver+", please reload the Java page to make Microscope Selector displayed properly."
				+ "This is due to a bug in Netscape JavaScript implementation.\n This is corrected starting from Netscape version 6.0.\n"
			);
		}
	}
}

function SetupApplet() {
	var appletHeight;
	if (navigator.appName.indexOf("Microsoft") != -1)
 			appletHeight=document.body.clientHeight-70;
 	else	
			appletHeight=innerHeight-70;
	document.writeln('<applet align=center name="ctfApplet" code="ctf.class" archive="jcm-config-jan-2001.jar" width="100%" height="' + appletHeight +'"></applet>');	
}
		
function Scope(name,vol,cs,cc,de,di,vm,dr) {
	this.ScopeName=name;
	this.Voltage=vol;
	this.Cs=cs;
	this.Cc=cc;
	this.dE=de;
	this.dI=di;
	this.VerticalMotion=vm;
	this.Drift=dr;
}

var AllScopes = new Array();

function ReadFromCookie() {
	var cookie=document.cookie;
	if (cookie == "") return false;

	var start = cookie.indexOf("scope=");
	if (start == -1) return false;   // Cookie not defined for this page.
	start += 6;  // Skip name and equals sign.
	var end = cookie.indexOf(';', start);
	if (end == -1) end = cookie.length;
	var cookieval = cookie.substring(start, end);

	var scopes=cookieval.split('#');
	var count=0;
	for(var s=0;s<scopes.length;s++){
		var a = scopes[s].split('&');    // Break it into array of name/value pairs.
		var newscope=new Scope();
		for(var i=0; i < a.length; i++) { // Break each pair into an array.
       		a[i] = a[i].split(':');
			newscope[a[i][0]]=unescape(a[i][1]);
		}
		if(!isNaN(newscope.Voltage) && !isNaN(newscope.Cs) &&
		    !isNaN(newscope.Cc) && !isNaN(newscope.dE) &&
			!isNaN(newscope.dI) && !isNaN(newscope.VerticalMotion) &&
			!isNaN(newscope.Drift)) {
			AllScopes[count++]=newscope;
		}
	}
}

function StoreToCookie() {
	var cookieval = "";
	for(var s=0;s<AllScopes.length;s++) {
		if(cookieval != "") cookieval += '#';
		var scopeval="";
		if( !isNaN(AllScopes[s].Voltage) && !isNaN(AllScopes[s].Cs) &&
		    !isNaN(AllScopes[s].Cc) && !isNaN(AllScopes[s].dE) &&
			!isNaN(AllScopes[s].dI) && !isNaN(AllScopes[s].VerticalMotion) &&
			!isNaN(AllScopes[s].Drift)) {
			for(var prop in AllScopes[s]) {
	        	if (scopeval != "") scopeval += '&';
    			scopeval += prop + ':' + escape(AllScopes[s][prop]);
			}
			cookieval+=scopeval;
		}
	}

	var cookie = "scope=" + cookieval;
    cookie += "; expires=" + (new Date((new Date()).getTime() + 24*30*12*3600000)).toGMTString();
    document.cookie = cookie;		

    return true;
}

function setScope(select) {
	var index=select.selectedIndex;
	document.ctfApplet.setScopePara(select.options[index].value);
}

function InitSelectScope(select) {
	if(AllScopes.length==0){
		AllScopes[0]=new Scope("Titan Krios","300", "2.7", "2.7", "1", "1", "0", "0");
		AllScopes[1]=new Scope("JEOL3200","300", "4.1", "2.2", "0.9", "1", "50", "0");
	}
	select.options.length=0;
	for(var s=0; s<AllScopes.length; s++) {
		text=AllScopes[s].ScopeName;
		var val=AllScopes[s].Voltage;
		val+=" "+AllScopes[s].Cs;
		val+=" "+AllScopes[s].Cc;
		val+=" "+AllScopes[s].dE;
		val+=" "+AllScopes[s].dI;
		val+=" "+AllScopes[s].VerticalMotion;
		val+=" "+AllScopes[s].Drift;
		select.options[s]=new Option(text,val);
	}
	select.selectedIndex=0
	setScope(select)
}

function SaveScope(select) {
	var index=select.selectedIndex;
	var ret=new String(document.ctfApplet.getScopePara());
	var para=ret.split(" ");
	if(!isNaN(para[0]) && !isNaN(para[1]) && !isNaN(para[2]) && !isNaN(para[3]) && !isNaN(para[4]) 
	   && !isNaN(para[5]) && !isNaN(para[6]) ) { 
		AllScopes[index].Voltage=para[0];
		AllScopes[index].Cs=para[1];
		AllScopes[index].Cc=para[2];
		AllScopes[index].dE=para[3];
		AllScopes[index].dI=para[4];
		AllScopes[index].VerticalMotion=para[5];
		AllScopes[index].Drift=para[6];
		InitSelectScope(select);
		select.selectedIndex=index;
		return true;
	}
	else {
		alert("Bad parameters found in \""+ ret +"\", won't save!");
		return false;
	}
}

function AddScope(select) {
	var index=AllScopes.length;
	var scopename=prompt("Please input name for this microscope:","scope"+index);
	if(scopename==null || scopename=="") return false;
	var ret=new String(document.ctfApplet.getScopePara());
	var para=ret.split(" ");
	if(!isNaN(para[0]) && !isNaN(para[1]) && !isNaN(para[2]) && !isNaN(para[3]) && !isNaN(para[4]) 
	   && !isNaN(para[5]) && !isNaN(para[6]) ) { 
		AllScopes[index]=new Scope(scopename,para[0],para[1],para[2],para[3],para[4],para[5],para[6]);
		InitSelectScope(select);
		select.selectedIndex=index;
		return true;
	}
	else {
		alert("Bad parameters found in \""+ ret +"\", won't save!");
		return false;
	}
}

function RemoveScope(select) {
	if(select.selectedIndex>AllScopes.length-1) {
		alert("Internal error: number of micorscopes = "+AllScopes.length+
			", but trying to delete microscope "+ select.selectedIndex+"!");
		return false;
	} 
	var index=select.selectedIndex;
	AllScopes.splice(index,1);
	InitSelectScope(select);
	
	if(index>0) select.selectedIndex=index-1;
	else		select.selectedIndex=0;
	setScope(select);
	return true;
}		
	
function setInfoVisible(form) {
    if(form.showinfo.value == "Show Info") {
        document.ctfApplet.setInfoVisible(true);
        form.showinfo.value = "Hide Info";
    } else {
        document.ctfApplet.setInfoVisible(false);
        form.showinfo.value = "Show Info";
    }         
}

function setFunction(form) {
     var index=form.selectfunction.selectedIndex;
     if(index>=1 && index <=6)    
         document.ctfApplet.setYLimit(0,1);
     else
         document.ctfApplet.setYLimit(-1,1);
     document.ctfApplet.setFunction(form.selectfunction.options[index].value,true);
}

function editFunction(form,button) {
     var string=document.ctfApplet.getFunctionString() + button.value;
     document.ctfApplet.setFunction(string,false);
}

function setIntensity(form) {
     var string="("+document.ctfApplet.getFunctionString() +")**2";
     document.ctfApplet.setYLimit(0,1);
     document.ctfApplet.setFunction(string,true);
}

function getGraphPoints(form) {
   var ct = eval(form.numpoints.value);
   var xmin = eval(form.xmin.value);
   var xmax = eval(form.xmax.value);
   var str = "x\ty\n";
   for (var i = 0; i <=ct; i++) {
         var x = i/ct*(xmax-xmin)+xmin; 
      var x2= Math.round(x*100000)/100000;
      str = str + x2 + "\t" + document.ctfApplet.getY(x) + "\n";
   } 
   form.pointDisplay.value = str;
   form.pointDisplay.focus();
   form.pointDisplay.select();            
}    
// -->

