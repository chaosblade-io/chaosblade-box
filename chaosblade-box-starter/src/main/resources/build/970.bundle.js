(self.webpackChunk=self.webpackChunk||[]).push([[970],{43418:(y,i,u)=>{"use strict";Object.defineProperty(i,"__esModule",{value:!0});var E=u(30156);i.default=E.Collapse},30553:(y,i,u)=>{"use strict";Object.defineProperty(i,"__esModule",{value:!0});var E=u(30156);i.default=E.Radio},19e3:function(y,i,u){var E,h,m,A=u(24596),f=u(67394),F=u(93168),k=u(23587),H=u(83452),L=u(95315),Q=u(63774),ue=u(92937);(function(D,x){if(!0)!(h=[i,u(17225),u(28757),u(35049),u(77809),u(57379),u(81853),u(27378),u(14870),u(73262),u(74427)],E=x,m=typeof E=="function"?E.apply(i,h):E,m!==void 0&&(y.exports=m));else var e})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(D,x,e,b,n,V,I,t,le,Z,B){"use strict";var W=u(67971);f(D,"__esModule",{value:!0}),D.default=void 0,x=W(x),e=W(e),b=W(b),n=W(n),V=W(V),I=W(I),t=P(t),B=W(B);function _(a){if(typeof F!="function")return null;var s=new F,l=new F;return(_=function(p){return p?l:s})(a)}function P(a,s){if(!s&&a&&a.__esModule)return a;if(a===null||A(a)!=="object"&&typeof a!="function")return{default:a};var l=_(s);if(l&&l.has(a))return l.get(a);var r={},p=f&&k;for(var O in a)if(O!=="default"&&Object.prototype.hasOwnProperty.call(a,O)){var U=p?k(a,O):null;U&&(U.get||U.set)?f(r,O,U):r[O]=a[O]}return r.default=a,l&&l.set(a,r),r}function C(a,s){var l=H(a);if(L){var r=L(a);s&&(r=r.filter(function(p){return k(a,p).enumerable})),l.push.apply(l,r)}return l}function c(a){for(var s=1;s<arguments.length;s++){var l=arguments[s]!=null?arguments[s]:{};s%2?C(Object(l),!0).forEach(function(r){(0,V.default)(a,r,l[r])}):Q?ue(a,Q(l)):C(Object(l)).forEach(function(r){f(a,r,k(l,r))})}return a}var w=function(s){var l=s.params,r=s.value,p=s.appInfo,O=s.placeholder,U=s.onChange,J=(0,le.useDispatch)(),oe=(0,t.useState)([]),te=(0,I.default)(oe,2),R=te[0],G=te[1],z=(0,t.useRef)(1),q=(0,t.useRef)(!1),ie=(0,t.useState)(!1),X=(0,I.default)(ie,2),de=X[0],j=X[1],N=(0,t.useState)(0),$=(0,I.default)(N,2),ne=$[0],v=$[1],re=(0,t.useState)(""),T=(0,I.default)(re,2),fe=T[0],me=T[1],Be=(0,t.useState)(!1),ce=(0,I.default)(Be,2),Pe=ce[0],he=ce[1],De=(0,t.useState)(r),pe=(0,I.default)(De,2),Re=pe[0],Ae=pe[1],Fe=(0,t.useRef)(c({},l));(0,t.useEffect)(function(){Ae(s.value),me("")},[s.value]);var Ee=function(){var o=function(){var ee=(0,n.default)(regeneratorRuntime.mark(function Y(){var K,M,se,ve;return regeneratorRuntime.wrap(function(ae){for(;;)switch(ae.prev=ae.next){case 0:return j(!0),ae.next=3,J.experimentDataSource.getApplication(c(c({},Fe.current),{},{page:z.current,size:11,key:fe}));case 3:K=ae.sent,M=K.data,se=K.pages,ve=K.total,(M==null?void 0:M.length)>0?(M==null||M.map(function(g){return g.value=g.app_id,g.label=g.app_name,g.scopesType=g.scope_type,g.appType=g.app_type,g.osType=g.os_type,g}),G(function(g){return[].concat((0,b.default)(g),(0,b.default)(M))})):ve===0&&G([]),v(se),j(!1),q.current=!1;case 9:case"end":return ae.stop()}},Y)}));return function(){return ee.apply(this,arguments)}}();(l.appType!==void 0||l.osType!==void 0&&!isNaN(l.osType))&&o()};(0,t.useEffect)(function(){Ee()},[fe]),(0,t.useEffect)(function(){z.current=1,Fe.current=l,G([]),Ee()},[l.osType,l.appType]),(0,t.useEffect)(function(){var d=document.querySelector("#selectScroll .next-menu");return setTimeout(function(){var o;d=document.querySelector("#selectScroll .next-menu"),(o=d)===null||o===void 0||o.addEventListener("scroll",Ce)},300),function(){var o;(o=d)===null||o===void 0||o.removeEventListener("scroll",Ce)}},[Pe]);var Ce=function(o){var ee=o.target,Y=ee||{},K=Y.scrollTop,M=Y.offsetHeight,se=Y.scrollHeight;ee&&K+M>60&&K+M>se&&(!q.current&&z.current<ne&&(z.current=z.current+1,q.current=!0,Ee()))};function _e(d,o){return o===Z.SCOPE_TYPE.HOST?d===Z.OS_TYPE.LINUX?"\u4E3B\u673A-linux":d===Z.OS_TYPE.WINDOWS?"\u4E3B\u673A-windows":"\u4E3B\u673A":o===Z.SCOPE_TYPE.K8S?"Kubernetes":""}function ge(d){return t.default.createElement("div",{className:B.default.itemContent,title:d&&d.label},t.default.createElement("div",{className:B.default.appName},d&&d.label),t.default.createElement("div",{className:B.default.scopeTip},_e(d.os_type,d.scope_type)))}return t.default.createElement(t.default.Fragment,null,t.default.createElement(e.default,{className:B.default.appSelect,showSearch:!0,filterLocal:!1,style:{width:"100%"},value:Re,placeholder:O||"",onChange:function(o,ee){var Y=R.find(function(K){return K.app_id===o});Ae(o),U==null||U(o,ee,Y)},fillProps:"app_id",onFocus:function(){return he(!0)},onSearch:function(o){j(!0),z.current=1,G([]),me(o)},dataSource:R,popupContainer:"selectScroll",itemRender:function(o){return ge(o)}},R==null?void 0:R.map(function(d,o){return t.default.createElement(e.default.Option,{value:d.app_id,key:o},t.default.createElement("div",{className:B.default.itemContent,title:d.label},t.default.createElement("div",{className:B.default.appName},d.label),t.default.createElement("div",{className:B.default.scopeTip},_e(d.os_type,d.scope_type))))}),(R==null?void 0:R.length)===0&&(p==null?void 0:p.appId)&&t.default.createElement(e.default.Option,{value:p.appId},t.default.createElement("div",{className:B.default.itemContent,title:p.appName},t.default.createElement("div",{className:B.default.appName},p.appName),t.default.createElement("div",{className:B.default.scopeTip},_e(p.osType,p.scopeType)))),de&&t.default.createElement(e.default.Option,{value:"more",key:"more"},t.default.createElement("div",{style:{color:"#0064C8"}},t.default.createElement(x.default,{size:"small",type:"loading"}),"\xA0\xA0\u52A0\u8F7D\u66F4\u591A\u9009\u9879..."))),t.default.createElement("div",{id:"selectScroll"}))},S=w;D.default=S})},9644:function(y,i,u){var E,h,m,A=u(24596),f=u(67394),F=u(93168),k=u(23587);(function(H,L){if(!0)!(h=[i,u(12955),u(17534),u(77809),u(43418),u(81853),u(27378),u(36012),u(34480),u(73262),u(14870)],E=L,m=typeof E=="function"?E.apply(i,h):E,m!==void 0&&(y.exports=m));else var Q})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(H,L,Q,ue,D,x,e,b,n,V,I){"use strict";var t=u(67971);f(H,"__esModule",{value:!0}),H.default=void 0,L=t(L),Q=t(Q),ue=t(ue),D=t(D),x=t(x),e=Z(e),b=t(b),n=t(n);function le(_){if(typeof F!="function")return null;var P=new F,C=new F;return(le=function(w){return w?C:P})(_)}function Z(_,P){if(!P&&_&&_.__esModule)return _;if(_===null||A(_)!=="object"&&typeof _!="function")return{default:_};var C=le(P);if(C&&C.has(_))return C.get(_);var c={},w=f&&k;for(var S in _)if(S!=="default"&&Object.prototype.hasOwnProperty.call(_,S)){var a=w?k(_,S):null;a&&(a.get||a.set)?f(c,S,a):c[S]=_[S]}return c.default=_,C&&C.set(_,c),c}var B=function(P){var C=P.pluginType,c=P.isUninstall,w=P.configurationId,S=P.onClose,a=P.isInstall,s=P.isClusterUninstall,l=P.ostype,r=(0,I.useDispatch)(),p=(0,e.useState)(""),O=(0,x.default)(p,2),U=O[0],J=O[1],oe=(0,e.useState)(""),te=(0,x.default)(oe,2),R=te[0],G=te[1],z=(0,e.useMemo)(function(){return e.default.createElement(D.default,{accordion:!0},e.default.createElement(D.default.Panel,{title:"\u975E\u5BB9\u5668\u5316\u5E94\u7528\u670D\u52A1\u5668"},e.default.createElement("div",null,"\u5728JVM\u542F\u52A8\u53C2\u6570\u4E2D\u53BB\u9664\u4EE5\u4E0B\u53C2\u6570\u5E76\u542F\u52A8JVM \u5373\u53EF\uFF1A"),e.default.createElement("pre",null,"-Dproject.name=<AppName> -javaagent:/opt/aliyunahas/agent/ahas-java-agent.jar")),e.default.createElement(D.default.Panel,{title:"Docker"},e.default.createElement("div",null,"\u6B65\u9AA4\u4E00"),e.default.createElement("div",null,"\u901A\u8FC7docker ps -a \u627E\u5230ahas java agent\u5BB9\u5668\u5E76\u5220\u9664"),e.default.createElement("pre",null,"sudo docker rm <dockerID>"),e.default.createElement("div",null,"\u6B65\u9AA4\u4E8C"),e.default.createElement("div",null,"\u505C\u6B62\u5E76\u5220\u9664\u542B\u6709ahas java agent\u7684\u5E94\u7528\u5BB9\u5668\uFF0C\u91CD\u65B0\u8FD0\u884C\u4E0D\u542B\u6709\u4E0B\u5217\u53C2\u6570\u7684\u5E94\u7528\u5BB9\u5668"),e.default.createElement("pre",null,'--volumes-from ahas-java-agent \\ --env JAVA_OPTS="-Dproject.name=<AppName> -javaagent:/var/lib/aliyunahas/agent/ahas-java-agent.jar"')),e.default.createElement(D.default.Panel,{title:"Docker Compose"},e.default.createElement("div",null,"\u8FD8\u539F\u4E4B\u524D\u5907\u4EFD\u7684docker-compose.yaml\u6587\u4EF6\uFF0C\u5E76\u91CD\u65B0\u62C9\u8D77docker")),e.default.createElement(D.default.Panel,{title:"Sentinel SDK pom\u4F9D\u8D56\u65B9\u5F0F"},e.default.createElement("div",null,"\u5982\u679C\u4F7F\u7528 POM \u4F9D\u8D56\u65B9\u5F0F\u63A5\u5165\u7684 Sentinel SDK\uFF0C\u76F4\u63A5\u53BB\u6389 POM \u4F9D\u8D56\uFF1A"),e.default.createElement("pre",null,"<dependency>",e.default.createElement("br",null),"\xA0\xA0<groupId>com.alibaba.csp</groupId>",e.default.createElement("br",null),"\xA0\xA0<artifactId>sentinel-transport-ahas-gw</artifactId>",e.default.createElement("br",null),"\xA0\xA0<version>$","{\u5F53\u524D\u7248\u672C\u53F7}","</version>",e.default.createElement("br",null),"</dependency>")),e.default.createElement(D.default.Panel,{title:"Sentinel SDK jar\u5305\u4F9D\u8D56\u65B9\u5F0F"},e.default.createElement("div",null,"\u5982\u679C\u4F7F\u7528\u7684 jar \u5305\u4F9D\u8D56\uFF0C\u8BF7\u76F4\u63A5\u5728\u5DE5\u7A0B\u4E2D\u53BB\u6389 Sentinel \u76F8\u5173\u7684 jar \u5305\u5373\u53EF\u3002")))},[]),q=(0,e.useCallback)((0,ue.default)(regeneratorRuntime.mark(function j(){var N,$,ne,v;return regeneratorRuntime.wrap(function(T){for(;;)switch(T.prev=T.next){case 0:if($={Mode:"host",OsType:l},c?N="QueryUninstallCommand":N="QueryInstallCommand",a||($.ConfigurationId=w),!s){T.next=7;break}J("helm delete --purge agent"),T.next=14;break;case 7:return T.next=9,r.agentSetting.getQueryUninstallAndInstallCommand(N,$);case 9:ne=T.sent,v=ne.Data,v&&v.command_install&&J(v&&v.command_install),v&&v.command_uninstall&&J(v&&v.command_uninstall),v&&v.command_file_download&&G(v&&v.command_file_download);case 14:case"end":return T.stop()}},j)})),[C,c,w,s]);(0,e.useEffect)(function(){(C==null?void 0:C.toUpperCase())==="CHAOS_AGENT"||a?q():J(z)},[q]);function ie(){if(l===V.OS_TYPE.WINDOWS&&!c)return e.default.createElement("div",null,e.default.createElement("div",{className:n.default.title},"before. \u5982\u679C\u66FE\u5B89\u88C5\u8FC7\u63A2\u9488"),e.default.createElement("div",{className:n.default.item},"1. \u6E05\u9664\u65E7\u63A2\u9488\uFF1Awin\u952E+R \u8F93\u5165cmd\uFF0C\u6253\u5F00cmd\uFF08Windows\u547D\u4EE4\u884C\uFF09\uFF0C\u5728\u547D\u4EE4\u884C\u4E2D\u5229\u7528taskkill\uFF0C\u6740\u6389\u539F\u6709\u7684\u65E7\u63A2\u9488"),e.default.createElement("div",{className:n.default.code},"taskkill /im aliyunahas.exe -f"),e.default.createElement("div",{className:n.default.item},"2. \u6E05\u9664\u6545\u969C\u6CE8\u5165\u5DE5\u5177\uFF1A\u8FDB\u5165C:\\\u4E2D\uFF0C\u67E5\u770B\u5F53\u524D\u76EE\u5F55\u4E0B\u662F\u5426\u6709chaosblade\u6587\u4EF6\u5939\uFF0C\u6216\u4EE5chaosblade\u5F00\u5934\u7684\u538B\u7F29\u5305\uFF0C\u5982\u679C\u6709\u5219\u90FD\u5220\u9664\u6389"),e.default.createElement("div",{className:n.default.title},"1. \u4E0B\u8F7D"),e.default.createElement("div",{className:n.default.item},"\u53EF\u76F4\u63A5\u5728\u6D4F\u89C8\u5668\u4E0A\u8F93\u5165\u4EE5\u4E0B\u5730\u5740\uFF0C\u76F4\u63A5\u8FDB\u884C\u4E0B\u8F7D\uFF0C\u4E0B\u8F7D\u5F97\u5230 aliyunahas.zip \uFF0C\u5C06\u6587\u4EF6\u590D\u5236\u5230C:\\\u4E2D"),e.default.createElement("div",{className:n.default.item},"\xB7 aliyunahas.exe\u4E0B\u8F7D\u5730\u5740\uFF1A ",e.default.createElement("a",{href:R,download:!0,target:"_blank"},R)),e.default.createElement("div",{className:n.default.title},"2. \u89E3\u538B"),e.default.createElement("div",{className:n.default.item},"1. \u5C06\u6587\u4EF6\u89E3\u538B\u5230C\u76D8\u7684C:\\\u76EE\u5F55\u4E0B"),e.default.createElement("div",{className:n.default.item},"2. \u5C06\u89E3\u538B\u540E\bC:\\aliyunahas\u6587\u4EF6\u5939\u4E2D\u7684\uFF0CC:\\aliyunahas\\chaosblade\u6587\u4EF6\u5939\u79FB\u81F3C:\\\u76EE\u5F55\u4E0B"),e.default.createElement("div",{className:n.default.code},"mv C:\\aliyunahas\\chaosblade C:\\"),e.default.createElement("div",{className:n.default.title},"3. \u542F\u52A8\u63A2\u9488"),e.default.createElement("div",{className:n.default.item},"win\u952E+R \u8F93\u5165cmd\uFF0C\u6253\u5F00cmd\uFF08Windows\u547D\u4EE4\u884C\uFF09\uFF0C\u5728\u547D\u4EE4\u884C\u4E2D\u542F\u52A8\u63A2\u9488"),e.default.createElement("div",{className:n.default.code},U),e.default.createElement("div",{className:n.default.title},"\u53C2\u6570\u8BF4\u660E\uFF1A"),e.default.createElement("div",{className:n.default.item},"\xB7 appInstance\uFF1A\u5E94\u7528\u540D\u79F0\uFF0C\u81EA\u5B9A\u4E49\u5373\u53EF"),e.default.createElement("div",{className:n.default.item},"\xB7 appGroup\uFF1A \u5E94\u7528\u5206\u7EC4\u540D\u79F0\uFF0C\u81EA\u5B9A\u4E49\u5373\u53EF"),e.default.createElement("div",{className:n.default.item},"\u786E\u5B9A\u63A2\u9488\u662F\u5426\u542F\u52A8\u6210\u529F\uFF0Cwin\u952E+R \u8F93\u5165cmd\uFF0C\u6253\u5F00cmd\uFF08Windows\u547D\u4EE4\u884C\uFF09\uFF0C\u5728cmd\u4E2D\u5229\u7528 tasklist \u6765\u67E5\u770B\u8FDB\u7A0B\u8FD0\u884C\u60C5\u51B5\uFF1A"),e.default.createElement("div",{className:n.default.code},e.default.createElement("div",null,"C:\\Users\\Administrator",">",'tasklist /FI "IMAGENAME eq aliyunahas.exe" /NH'),e.default.createElement("div",{className:n.default.command},e.default.createElement("span",null,"aliyunahas.exe"),e.default.createElement("span",null,"5928 RDP-Tcp#16"),e.default.createElement("span",null,"2\u2003\u2003\u200323,428 K"))));if(l===V.OS_TYPE.WINDOWS&&c)return e.default.createElement("div",null,e.default.createElement("div",{className:n.default.title,style:{marginBottom:8}},"\u624B\u52A8\u5378\u8F7D\u63A2\u9488\u63A2\u9488"),e.default.createElement("div",{className:n.default.item},"1. \u6E05\u9664\u65E7\u63A2\u9488\uFF1Awin\u952E+R \u8F93\u5165cmd\uFF0C\u6253\u5F00cmd\uFF08Windows\u547D\u4EE4\u884C\uFF09\uFF0C\u5728\u547D\u4EE4\u884C\u4E2D\u5229\u7528taskkill\uFF0C\u6740\u6389\u539F\u6709\u7684\u65E7\u63A2\u9488"),e.default.createElement("div",{className:n.default.code},"taskkill /im aliyunahas.exe -f"),e.default.createElement("div",{className:n.default.item},"2. \u6E05\u9664\u6545\u969C\u6CE8\u5165\u5DE5\u5177\uFF1A\u8FDB\u5165C:\\\u4E2D\uFF0C\u67E5\u770B\u5F53\u524D\u76EE\u5F55\u4E0B\u662F\u5426\u6709chaosblade\u6587\u4EF6\u5939\uFF0C\u6216\u4EE5chaosblade\u5F00\u5934\u7684\u538B\u7F29\u5305\uFF0C\u5982\u679C\u6709\u5219\u90FD\u5220\u9664\u6389"));if(a||c&&!s)return e.default.createElement("div",{style:{position:"relative"}},e.default.createElement("div",{className:n.default.copyBtn},e.default.createElement("a",{onClick:function(){return de(U)}},"\u70B9\u51FB\u590D\u5236")),e.default.createElement("div",{style:{paddingTop:20}},U));var j=`blades=($(kubectl get blade | grep -v NAME | awk '{print $1}' | tr '
' ' ')) && kubectl patch blade $blades --type merge -p '{"metadata":{"finalizers":[]}}'`;return e.default.createElement("div",null,e.default.createElement("div",{className:n.default.item},"1. \u6267\u884C\u4EE5\u4E0BHelm\u547D\u4EE4\u5378\u8F7D\u63A2\u9488"),X("helm un agent -n chaosblade",!0),e.default.createElement("div",{className:n.default.item},"2. \u5378\u8F7D\u5B8C\u6210\u540E\uFF0C\u53EF\u6267\u884C\u4E00\u4E0B\u547D\u4EE4\u67E5\u8BE2ahas\u547D\u4EE4\u7A7A\u95F4\u7684\u63A2\u9488pod\u662F\u5426\u5DF2\u5378\u8F7D\u5B8C\u6210"),X("kubectl get pods -n chaosblade",!0),e.default.createElement("div",{className:n.default.item},"3. \u5982\u679C\u5378\u8F7D\u5F02\u5E38\uFF0C\u5728\u786E\u4FDD\u6240\u6709\u6F14\u7EC3\u5DF2\u7EC8\u6B62\u7684\u60C5\u51B5\u4E0B\uFF0C\u6267\u884C\u4EE5\u4E0B\u547D\u4EE4\u5220\u9664\u5F02\u5E38\u72B6\u6001\u7684\u6F14\u7EC3"),X(j,!0),e.default.createElement("div",{className:n.default.item},"4. \u6267\u884C\u540E\u53EF\u6267\u884C\u4E0B\u9762\u547D\u4EE4\u786E\u8BA4\u6240\u6709\u7684chaosblade\u8D44\u6E90\u5747\u88AB\u5220\u9664",e.default.createElement("a",{href:R,download:!0,target:"_blank"},R)),X("kubectl get chaosblade",!0))}var X=function(N,$){return e.default.createElement("div",{className:n.default.code},$&&e.default.createElement("div",{className:n.default.copyBtn},e.default.createElement("a",{onClick:function(){return de(N)}},"\u70B9\u51FB\u590D\u5236")),N)},de=function(N){(0,b.default)(N),Q.default.success("\u590D\u5236\u6210\u529F")};return e.default.createElement(L.default,{visible:!0,title:c?"\u5355\u673A\u624B\u52A8\u5378\u8F7D\u63D2\u4EF6":"\u5355\u673A\u624B\u52A8\u5B89\u88C5\u63D2\u4EF6",footerActions:["cancel"],style:{minWidth:"600px"},onClose:S,onCancel:S,shouldUpdatePosition:!0},e.default.createElement("div",{className:n.default.content},ie()))},W=(0,e.memo)(B);H.default=W})},78576:(y,i,u)=>{"use strict";u.d(i,{Z:()=>F});var E=u(60994),h=u.n(E),m=u(93476),A=u.n(m),f=A()(h());f.push([y.id,`.index__itemContent__z8YF9 {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.index__scopeTip__JF-e2 {
  color: #c1c1c1;
  margin-right: 8px;
}

.index__appName__CfdNm {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}
.index__appSelect__cjp84 em {
    display: block;
    width: 100%;
  }
.index__appSelect__cjp84 .index__scopeTip__JF-e2{
    display: none;
  }`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/common/CustomSelect/index.css"],names:[],mappings:"AAAA;EACE,aAAa;EACb,8BAA8B;EAC9B,mBAAmB;AACrB;;AAEA;EACE,cAAc;EACd,iBAAiB;AACnB;;AAEA;EACE,gBAAgB;EAChB,uBAAuB;EACvB,WAAW;AACb;AAEE;IACE,cAAc;IACd,WAAW;EACb;AACA;IACE,aAAa;EACf",sourcesContent:[`.itemContent {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.scopeTip {
  color: #c1c1c1;
  margin-right: 8px;
}

.appName {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}
.appSelect {
  em {
    display: block;
    width: 100%;
  }
  .scopeTip{
    display: none;
  }
}`],sourceRoot:""}]),f.locals={itemContent:"index__itemContent__z8YF9",scopeTip:"index__scopeTip__JF-e2",appName:"index__appName__CfdNm",appSelect:"index__appSelect__cjp84"};const F=f},91814:(y,i,u)=>{"use strict";u.d(i,{Z:()=>F});var E=u(60994),h=u.n(E),m=u(93476),A=u.n(m),f=A()(h());f.push([y.id,`.index__content__ChFWv {
  display: block;
  padding: 9.5px;
  margin: 0 0 10px;
  font-size: 13px;
  line-height: 1.428571429;
  color: #333333;
  word-break: break-all;
  word-wrap: break-word;
  border: 1px solid #cccccc;
}

.index__title__nSbjq {
  color: #000;
  font-size: 14px; 
  margin-bottom: 6px;
}

.index__item__A8j2M {
  font-size: 12px;
  line-height: 22px;
  color: #333333;
  margin-bottom: 4px;
}

.index__code__ezF32 {
  width: 100%;
  padding: 18px 12px;
  background: #f9f9f9;
  border: 1px solid #e8e8e8;
  margin-bottom: 16px;
  position: relative;
}
.index__copyBtn__usM0e {
  font-size: 12px;
  float: right;
  position: absolute;
  right: 8px;
  top: 4px;
  cursor: pointer;
}

.index__command__bQlmQ {
  width: 70%;
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
}`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/common/ManualDialog/index.css"],names:[],mappings:"AAAA;EACE,cAAc;EACd,cAAc;EACd,gBAAgB;EAChB,eAAe;EACf,wBAAwB;EACxB,cAAc;EACd,qBAAqB;EACrB,qBAAqB;EACrB,yBAAyB;AAC3B;;AAEA;EACE,WAAW;EACX,eAAe;EACf,kBAAkB;AACpB;;AAEA;EACE,eAAe;EACf,iBAAiB;EACjB,cAAc;EACd,kBAAkB;AACpB;;AAEA;EACE,WAAW;EACX,kBAAkB;EAClB,mBAAmB;EACnB,yBAAyB;EACzB,mBAAmB;EACnB,kBAAkB;AACpB;AACA;EACE,eAAe;EACf,YAAY;EACZ,kBAAkB;EAClB,UAAU;EACV,QAAQ;EACR,eAAe;AACjB;;AAEA;EACE,UAAU;EACV,gBAAgB;EAChB,aAAa;EACb,8BAA8B;AAChC",sourcesContent:[`.content {
  display: block;
  padding: 9.5px;
  margin: 0 0 10px;
  font-size: 13px;
  line-height: 1.428571429;
  color: #333333;
  word-break: break-all;
  word-wrap: break-word;
  border: 1px solid #cccccc;
}

.title {
  color: #000;
  font-size: 14px; 
  margin-bottom: 6px;
}

.item {
  font-size: 12px;
  line-height: 22px;
  color: #333333;
  margin-bottom: 4px;
}

.code {
  width: 100%;
  padding: 18px 12px;
  background: #f9f9f9;
  border: 1px solid #e8e8e8;
  margin-bottom: 16px;
  position: relative;
}
.copyBtn {
  font-size: 12px;
  float: right;
  position: absolute;
  right: 8px;
  top: 4px;
  cursor: pointer;
}

.command {
  width: 70%;
  margin-top: 16px;
  display: flex;
  justify-content: space-between;
}`],sourceRoot:""}]),f.locals={content:"index__content__ChFWv",title:"index__title__nSbjq",item:"index__item__A8j2M",code:"index__code__ezF32",copyBtn:"index__copyBtn__usM0e",command:"index__command__bQlmQ"};const F=f},74427:(y,i,u)=>{"use strict";u.r(i),u.d(i,{default:()=>F});var E=u(1892),h=u.n(E),m=u(78576),A={};A.insert="head",A.singleton=!1;var f=h()(m.Z,A);const F=m.Z.locals||{}},34480:(y,i,u)=>{"use strict";u.r(i),u.d(i,{default:()=>F});var E=u(1892),h=u.n(E),m=u(91814),A={};A.insert="head",A.singleton=!1;var f=h()(m.Z,A);const F=m.Z.locals||{}}}]);

//# sourceMappingURL=970.bundle.js.map