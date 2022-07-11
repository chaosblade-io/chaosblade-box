(self.webpackChunk=self.webpackChunk||[]).push([[866],{30553:($,c,e)=>{"use strict";Object.defineProperty(c,"__esModule",{value:!0});var p=e(30156);c.default=p.Radio},9644:function($,c,e){var p,S,v,U=e(24596),A=e(67394),y=e(93168),z=e(23587);(function(V,j){if(!0)!(S=[c,e(12955),e(17534),e(77809),e(81853),e(27378),e(66697),e(36012),e(14798),e(34480),e(14870)],p=j,v=typeof p=="function"?p.apply(c,S):p,v!==void 0&&($.exports=v));else var G})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(V,j,G,ne,K,t,r,Q,T,n,X){"use strict";var D=e(67971);A(V,"__esModule",{value:!0}),V.default=void 0,j=D(j),G=D(G),ne=D(ne),K=D(K),t=de(t),r=D(r),Q=D(Q),T=D(T),n=D(n);function Y(i){if(typeof y!="function")return null;var l=new y,m=new y;return(Y=function(a){return a?m:l})(i)}function de(i,l){if(!l&&i&&i.__esModule)return i;if(i===null||U(i)!=="object"&&typeof i!="function")return{default:i};var m=Y(l);if(m&&m.has(i))return m.get(i);var R={},a=A&&z;for(var O in i)if(O!=="default"&&Object.prototype.hasOwnProperty.call(i,O)){var x=a?z(i,O):null;x&&(x.get||x.set)?A(R,O,x):R[O]=i[O]}return R.default=i,m&&m.set(i,R),R}var w=function(l){var m=l.pluginType,R=l.isUninstall,a=l.configurationId,O=l.onClose,x=l.isInstall,oe=l.isClusterUninstall,ue=l.ostype,_=(0,X.useDispatch)(),se=(0,t.useState)(""),ie=(0,K.default)(se,2),H=ie[0],J=ie[1],b=(0,t.useState)(""),re=(0,K.default)(b,2),k=re[0],me=re[1],fe=(0,t.useCallback)((0,ne.default)(regeneratorRuntime.mark(function u(){var g,B,F,C;return regeneratorRuntime.wrap(function(L){for(;;)switch(L.prev=L.next){case 0:if(B={Mode:"host",OsType:ue},R?g="QueryUninstallCommand":g="QueryInstallCommand",x||(B.ConfigurationId=a),!oe){L.next=7;break}J("helm delete --purge agent"),L.next=14;break;case 7:return L.next=9,_.agentSetting.getQueryUninstallAndInstallCommand(g,B);case 9:F=L.sent,C=F.Data,C&&C.command_install&&J(C&&C.command_install),C&&C.command_uninstall&&J(C&&C.command_uninstall),C&&C.command_file_download&&me(C&&C.command_file_download);case 14:case"end":return L.stop()}},u)})),[m,R,a,oe]);(0,t.useEffect)(function(){((m==null?void 0:m.toUpperCase())==="CHAOS_AGENT"||x)&&fe()},[fe]);function d(){if(!R)return t.default.createElement("div",null,t.default.createElement("div",{className:n.default.title},t.default.createElement(r.default,null,"Before. If probes were installed")),t.default.createElement("div",{className:n.default.title},"1. ",t.default.createElement(r.default,null,"Download")),t.default.createElement("div",{className:n.default.item},t.default.createElement(r.default,null,"Download link:")," ",t.default.createElement("a",{href:k,download:!0,target:"_blank"},k)),t.default.createElement("div",{className:n.default.title},"2. ",t.default.createElement(r.default,null,"Decompress")),t.default.createElement("div",{className:n.default.title},"3. ",t.default.createElement(r.default,null,"Start the probe")),t.default.createElement("div",{className:n.default.code},H),t.default.createElement("div",{className:n.default.title},t.default.createElement(r.default,null,"Parameter Description"),":"),t.default.createElement("div",{className:n.default.item},"\xB7 appInstance\uFF1A",t.default.createElement(r.default,null,"Application name, you can customize")),t.default.createElement("div",{className:n.default.item},"\xB7 appGroup\uFF1A ",t.default.createElement(r.default,null,"Application group name, you can customize it")),t.default.createElement("div",{className:n.default.code},t.default.createElement("div",{className:n.default.command},t.default.createElement("span",null,"5928 RDP-Tcp#16"),t.default.createElement("span",null,"2\u2003\u2003\u200323,428 K"))));if(x||R&&!oe)return t.default.createElement("div",{style:{position:"relative"}},t.default.createElement("div",{className:n.default.copyBtn},t.default.createElement("a",{onClick:function(){return o(H)}},t.default.createElement(r.default,null,"Click to copy"))),t.default.createElement("div",{style:{paddingTop:20}},H));var u=`blades=($(kubectl get blade | grep -v NAME | awk '{print $1}' | tr '
' ' ')) && kubectl patch blade $blades --type merge -p '{"metadata":{"finalizers":[]}}'`;return t.default.createElement("div",null,t.default.createElement("div",{className:n.default.item},"1. ",t.default.createElement(r.default,null,"Execute the following Helm command to uninstall the probe")),E("helm un agent -n chaosblade",!0),t.default.createElement("div",{className:n.default.item},"2. ",t.default.createElement(r.default,null,"After the uninstallation is complete, execute the following command to check whether the probe pod in the command space has been uninstalled")),E("kubectl get pods -n chaosblade",!0),t.default.createElement("div",{className:n.default.item},"3. ",t.default.createElement(r.default,null,"If the uninstallation is abnormal, after ensuring that all the drills have been terminated, execute the following command to delete the drill in the abnormal state")),E(u,!0),t.default.createElement("div",{className:n.default.item},"4. ",t.default.createElement(r.default,null,"After execution, execute the following command to confirm that all chaosblade resources are deleted"),t.default.createElement("a",{href:k,download:!0,target:"_blank"},k)),E("kubectl get chaosblade",!0))}var E=function(g,B){return t.default.createElement("div",{className:n.default.code},B&&t.default.createElement("div",{className:n.default.copyBtn},t.default.createElement("a",{onClick:function(){return o(g)}},t.default.createElement(r.default,null,"Click to copy"))),g)},o=function(g){(0,Q.default)(g),G.default.success(T.default.t("Copy successfully"))};return t.default.createElement(j.default,{visible:!0,title:R?T.default.t("Manually uninstall the plug-in").toString():T.default.t("Manually install the plug-in on a single machine").toString(),footerActions:["cancel"],style:{minWidth:"600px"},onClose:O,onCancel:O,shouldUpdatePosition:!0},t.default.createElement("div",{className:n.default.content},d()))},Z=(0,t.memo)(w);V.default=Z})},39620:function($,c,e){var p,S,v,U=e(24596),A=e(67394),y=e(93168),z=e(23587),V=e(83452),j=e(95315),G=e(63774),ne=e(92937);(function(K,t){if(!0)!(S=[c,e(12955),e(93080),e(72153),e(28757),e(15286),e(17534),e(77809),e(57379),e(88162),e(81853),e(30553),e(8583),e(27378),e(66697),e(98784),e(14798),e(13988),e(99328),e(14870),e(19e3)],p=t,v=typeof p=="function"?p.apply(c,S):p,v!==void 0&&($.exports=v));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(K,t,r,Q,T,n,X,D,Y,de,w,Z,i,l,m,R,a,O,x,oe,ue){"use strict";var _=e(67971);A(K,"__esModule",{value:!0}),K.default=void 0,t=_(t),r=_(r),Q=_(Q),T=_(T),n=_(n),X=_(X),D=_(D),Y=_(Y),de=_(de),w=_(w),Z=_(Z),i=_(i),l=ie(l),m=_(m),R=_(R),a=_(a),O=_(O),ue=_(ue);function se(d){if(typeof y!="function")return null;var E=new y,o=new y;return(se=function(g){return g?o:E})(d)}function ie(d,E){if(!E&&d&&d.__esModule)return d;if(d===null||U(d)!=="object"&&typeof d!="function")return{default:d};var o=se(E);if(o&&o.has(d))return o.get(d);var u={},g=A&&z;for(var B in d)if(B!=="default"&&Object.prototype.hasOwnProperty.call(d,B)){var F=g?z(d,B):null;F&&(F.get||F.set)?A(u,B,F):u[B]=d[B]}return u.default=d,o&&o.set(d,u),u}function H(d,E){var o=V(d);if(j){var u=j(d);E&&(u=u.filter(function(g){return z(d,g).enumerable})),o.push.apply(o,u)}return o}function J(d){for(var E=1;E<arguments.length;E++){var o=arguments[E]!=null?arguments[E]:{};E%2?H(Object(o),!0).forEach(function(u){(0,Y.default)(d,u,o[u])}):G?ne(d,G(o)):H(Object(o)).forEach(function(u){A(d,u,z(o,u))})}return d}var b=i.default.Item,re={labelCol:{fixedSpan:6},wrapperCol:{span:18}},k=Z.default.Group,me=function(E){var o=E.onClose,u=(0,oe.useDispatch)(),g=(0,l.useState)([]),B=(0,w.default)(g,2),F=B[0],C=B[1],_e=(0,l.useState)(!1),L=(0,w.default)(_e,2),ve=L[0],Ee=L[1],ge=de.default.useField(),Pe=(0,l.useState)({appType:1}),ce=(0,w.default)(Pe,2),P=ce[0],Ae=ce[1],Re=(0,l.useState)(null),he=(0,w.default)(Re,2),N=he[0],Ce=he[1];(0,l.useEffect)(function(){Ae(J(J({},P),{},{AppName:void 0,AppGroupName:void 0}))},[P==null?void 0:P.appType]),(0,l.useEffect)(function(){var M=function(){var h=(0,D.default)(regeneratorRuntime.mark(function f(){var s,I,q,ee,te,le,ae;return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:return q=(s=(I=(0,x.getParams)("ns"))!==null&&I!==void 0?I:window.curNamespace)!==null&&s!==void 0?s:"default",ee={namespace:q,region:(0,x.getActiveRegion)(),appType:"0"},N&&(ee.app_id=N.app_id),W.next=5,u.agentSetting.getGetUserApplicationGroups(ee);case 5:te=W.sent,le=te.Data,ae=le===void 0?[]:le,C(ae);case 9:case"end":return W.stop()}},f)}));return function(){return h.apply(this,arguments)}}();P!=null&&P.AppName&&(P==null?void 0:P.appType)===1&&(C([]),M())},[N==null?void 0:N.app_id]);var Ue=function(){var M=(0,D.default)(regeneratorRuntime.mark(function h(f){var s,I,q,ee,te,le,ae;return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:return Ee(!0),s=R.default.cloneDeep(f),s.appType===1&&(s.AppName=N==null?void 0:N.app_name,s.AppId=N==null?void 0:N.app_id),delete s.appType,W.next=6,u.agentSetting.getInstallPlugin(s);case 6:I=W.sent,q=I.Data,ee=q===void 0?!1:q,te=I.Code,le=te===void 0?"":te,Ee(!1),ee?(X.default.success(a.default.t("The probe is being installed, this may take a few minutes, please check later...")),o()):(ae=a.default.t("The automatic installation of the probe failed, please try to install it manually"),le==="plugin.instance.not.exist"&&(ae=a.default.t("The current instance does not exist, please select an available instance")),X.default.error(ae));case 13:case"end":return W.stop()}},h)}));return function(f){return M.apply(this,arguments)}}(),pe=function(h,f,s,I){I==="InstanceId"&&/^((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))$/.test(f)===!1?s(a.default.t("Please fill in the legitimate machine IP address")):I==="SshPort"&&!(/^[1-9]\d*$/.test(f)&&Number(f)>=1&&Number(f)<=65535)?s(a.default.t("Please fill in valid port")):["AppName","AppGroupName"].includes(I)&&/^[\w|\||-]*$/g.test(f)===!1?s("".concat(a.default.t("Please fill in legal"),"\b").concat(I==="AppName"?a.default.t("Application"):a.default.t("Group")).concat(a.default.t("Name"))):s()};return l.default.createElement(t.default,{visible:!0,title:a.default.t("Install the probe").toString(),style:{width:"680px"},okProps:{children:a.default.t("Install").toString()},footer:!1,onCancel:o,onClose:o},l.default.createElement("div",{className:O.default.installDialog},l.default.createElement(i.default,(0,r.default)({},re,{field:ge,onChange:function(h){Ae(h)}}),l.default.createElement(b,{required:!0,label:a.default.t("Application").toString()},l.default.createElement(k,{name:"appType",defaultValue:P.appType},l.default.createElement(Z.default,{value:1},l.default.createElement(m.default,null,"Existing application")),l.default.createElement(Z.default,{value:2},l.default.createElement(m.default,null,"Add application")))),l.default.createElement(b,{label:a.default.t("Choose application").toString(),required:!0},P.appType===1&&l.default.createElement(ue.default,{params:{filterDisabled:!0,appType:0},placeholder:a.default.t("Please select an app name").toString(),name:"AppName",value:P.AppName,onChange:function(h,f,s){Ce(s)}})||l.default.createElement(n.default,{value:P.AppName,placeholder:a.default.t("Please input application name").toString(),name:"AppName"})),l.default.createElement(b,{label:a.default.t("Application group").toString(),validator:function(h,f,s){return pe(h,f,s,"AppGroupName")}},P.appType===1&&l.default.createElement(T.default,{showSearch:!0,value:P.AppGroupName,style:{width:"100%"},dataSource:F,placeholder:a.default.t("Please select an app group"),name:"AppGroupName"})||l.default.createElement(n.default,{value:P.AppGroupName,placeholder:a.default.t("Please input application group").toString(),name:"AppGroupName"})),l.default.createElement(b,{label:a.default.t("IP").toString(),required:!0,validator:function(h,f,s){return pe(h,f,s,"InstanceId")}},l.default.createElement(n.default,{placeholder:a.default.t("Please input the machine ip").toString(),name:"InstanceId"})),l.default.createElement(b,{label:a.default.t("port").toString(),validator:function(h,f,s){return pe(h,f,s,"SshPort")}},l.default.createElement(n.default,{placeholder:a.default.t("Please enter the port"),name:"SshPort",defaultValue:22})),l.default.createElement(b,{label:a.default.t("SSH user").toString(),required:!0,validator:function(h,f,s){return pe(h,f,s,"SshUser")}},l.default.createElement(n.default,{placeholder:a.default.t("Please enter SSH user").toString(),name:"SshUser"})),l.default.createElement(b,{label:a.default.t("SSH password").toString()},l.default.createElement(n.default,{htmlType:"password",placeholder:a.default.t("Please enter SSH password").toString(),name:"SshPassword"})),l.default.createElement("div",{style:{textAlign:"right"}},l.default.createElement(i.default.Submit,{validate:!0,type:"primary",loading:ve,onClick:function(h){return Ue(h)},style:{marginRight:10}},l.default.createElement(m.default,null,"Install")),l.default.createElement(Q.default,{onClick:function(){return o()}},l.default.createElement(m.default,null,"cancel"))))))},fe=(0,l.memo)(me);K.default=fe})},91814:($,c,e)=>{"use strict";e.d(c,{Z:()=>y});var p=e(60994),S=e.n(p),v=e(93476),U=e.n(v),A=U()(S());A.push([$.id,`.index__content__ChFWv {
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
}`],sourceRoot:""}]),A.locals={content:"index__content__ChFWv",title:"index__title__nSbjq",item:"index__item__A8j2M",code:"index__code__ezF32",copyBtn:"index__copyBtn__usM0e",command:"index__command__bQlmQ"};const y=A},86901:($,c,e)=>{"use strict";e.d(c,{Z:()=>y});var p=e(60994),S=e.n(p),v=e(93476),U=e.n(v),A=U()(S());A.push([$.id,`.index__installDialog__bDteq label:before {
    margin-right: 4px;
    content: " ";
    display: inline-block;
    width: 6px;
  }`,"",{version:3,sources:["webpack://./pages/Manage/AutoInstall/index.css"],names:[],mappings:"AACE;IACE,iBAAiB;IACjB,YAAY;IACZ,qBAAqB;IACrB,UAAU;EACZ",sourcesContent:[`.installDialog {
  label:before {
    margin-right: 4px;
    content: " ";
    display: inline-block;
    width: 6px;
  } 
}`],sourceRoot:""}]),A.locals={installDialog:"index__installDialog__bDteq"};const y=A},34480:($,c,e)=>{"use strict";e.r(c),e.d(c,{default:()=>y});var p=e(1892),S=e.n(p),v=e(91814),U={};U.insert="head",U.singleton=!1;var A=S()(v.Z,U);const y=v.Z.locals||{}},13988:($,c,e)=>{"use strict";e.r(c),e.d(c,{default:()=>y});var p=e(1892),S=e.n(p),v=e(86901),U={};U.insert="head",U.singleton=!1;var A=S()(v.Z,U);const y=v.Z.locals||{}}}]);

//# sourceMappingURL=866.bundle.js.map