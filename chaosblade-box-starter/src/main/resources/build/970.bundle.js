(self.webpackChunk=self.webpackChunk||[]).push([[970],{30553:(N,m,e)=>{"use strict";Object.defineProperty(m,"__esModule",{value:!0});var c=e(30156);m.default=c.Radio},19e3:function(N,m,e){var c,R,A,v=e(24596),p=e(67394),h=e(93168),Y=e(23587),J=e(83452),z=e(95315),H=e(63774),te=e(92937);(function(T,t){if(!0)!(R=[m,e(17225),e(28757),e(35049),e(77809),e(57379),e(81853),e(27378),e(66697),e(14798),e(68055),e(14870),e(73262),e(74427)],c=t,A=typeof c=="function"?c.apply(m,R):c,A!==void 0&&(N.exports=A));else var d})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(T,t,d,w,L,Q,a,n,y,$,ne,ie,le,l){"use strict";var r=e(67971);p(T,"__esModule",{value:!0}),T.default=void 0,t=r(t),d=r(d),w=r(w),L=r(L),Q=r(Q),a=r(a),n=C(n),y=r(y),$=r($),ne=r(ne),l=r(l);function g(o){if(typeof h!="function")return null;var f=new h,u=new h;return(g=function(E){return E?u:f})(o)}function C(o,f){if(!f&&o&&o.__esModule)return o;if(o===null||v(o)!=="object"&&typeof o!="function")return{default:o};var u=g(f);if(u&&u.has(o))return u.get(o);var _={},E=p&&Y;for(var S in o)if(S!=="default"&&Object.prototype.hasOwnProperty.call(o,S)){var W=E?Y(o,S):null;W&&(W.get||W.set)?p(_,S,W):_[S]=o[S]}return _.default=o,u&&u.set(o,_),_}function K(o,f){var u=J(o);if(z){var _=z(o);f&&(_=_.filter(function(E){return Y(o,E).enumerable})),u.push.apply(u,_)}return u}function U(o){for(var f=1;f<arguments.length;f++){var u=arguments[f]!=null?arguments[f]:{};f%2?K(Object(u),!0).forEach(function(_){(0,Q.default)(o,_,u[_])}):H?te(o,H(u)):K(Object(u)).forEach(function(_){p(o,_,Y(u,_))})}return o}var I=function(f){var u=f.params,_=f.value,E=f.appInfo,S=f.placeholder,W=f.onChange,ue=(0,ie.useDispatch)(),G=(0,n.useState)([]),de=(0,a.default)(G,2),D=de[0],k=de[1],x=(0,n.useRef)(1),q=(0,n.useRef)(!1),X=(0,n.useState)(!1),O=(0,a.default)(X,2),Z=O[0],V=O[1],P=(0,n.useState)(0),re=(0,a.default)(P,2),F=re[0],Ce=re[1],Re=(0,n.useState)(""),ce=(0,a.default)(Re,2),me=ce[0],pe=ce[1],ye=(0,n.useState)(!1),Ee=(0,a.default)(ye,2),Ue=Ee[0],Be=Ee[1],Se=(0,n.useState)(_),Ae=(0,a.default)(Se,2),Oe=Ae[0],ve=Ae[1],he=(0,n.useRef)(U({},u));(0,n.useEffect)(function(){ve(f.value),pe("")},[f.value]);var fe=function(){var i=function(){var ee=(0,L.default)(regeneratorRuntime.mark(function b(){var j,M,se,Pe;return regeneratorRuntime.wrap(function(oe){for(;;)switch(oe.prev=oe.next){case 0:return V(!0),oe.next=3,ue.experimentDataSource.getApplication(U(U({},he.current),{},{page:x.current,size:11,key:me}));case 3:j=oe.sent,M=j.data,se=j.pages,Pe=j.total,(M==null?void 0:M.length)>0?(M==null||M.map(function(B){return B.value=B.app_id,B.label=B.app_name,B.scopesType=B.scope_type,B.appType=B.app_type,B.osType=B.os_type,B}),k(function(B){return[].concat((0,w.default)(B),(0,w.default)(M))})):Pe===0&&k([]),Ce(se),V(!1),q.current=!1;case 9:case"end":return oe.stop()}},b)}));return function(){return ee.apply(this,arguments)}}();(u.appType!==void 0||u.osType!==void 0&&!isNaN(u.osType))&&i()};(0,n.useEffect)(function(){fe()},[me]),(0,n.useEffect)(function(){x.current=1,he.current=u,k([]),fe()},[u.osType,u.appType]),(0,n.useEffect)(function(){var s=document.querySelector("#selectScroll .next-menu");return setTimeout(function(){var i;s=document.querySelector("#selectScroll .next-menu"),(i=s)===null||i===void 0||i.addEventListener("scroll",ge)},300),function(){var i;(i=s)===null||i===void 0||i.removeEventListener("scroll",ge)}},[Ue]);var ge=function(i){var ee=i.target,b=ee||{},j=b.scrollTop,M=b.offsetHeight,se=b.scrollHeight;ee&&j+M>60&&j+M>se&&(!q.current&&x.current<F&&(x.current=x.current+1,q.current=!0,fe()))};function _e(s,i){return i===le.SCOPE_TYPE.HOST?s===le.OS_TYPE.LINUX?$.default.t("Host-linux"):$.default.t("Host"):i===le.SCOPE_TYPE.K8S?$.default.t("Kubernetes"):""}function De(s){return n.default.createElement("div",{className:l.default.itemContent,title:s&&s.label},n.default.createElement("div",{className:l.default.appName},s&&s.label),n.default.createElement("div",{className:l.default.scopeTip},_e(s.os_type,s.scope_type)))}return n.default.createElement(n.default.Fragment,null,n.default.createElement(d.default,{className:l.default.appSelect,showSearch:!0,filterLocal:!1,style:{width:"100%"},value:Oe,placeholder:S||"",onChange:function(i,ee){var b=D.find(function(j){return j.app_id===i});ve(i),W==null||W(i,ee,b)},fillProps:"app_id",onFocus:function(){return Be(!0)},onSearch:function(i){V(!0),x.current=1,k([]),pe(i)},dataSource:D,popupContainer:"selectScroll",itemRender:function(i){return De(i)},locale:(0,ne.default)().Select},D==null?void 0:D.map(function(s,i){return n.default.createElement(d.default.Option,{value:s.app_id,key:i},n.default.createElement("div",{className:l.default.itemContent,title:s.label},n.default.createElement("div",{className:l.default.appName},s.label),n.default.createElement("div",{className:l.default.scopeTip},_e(s.os_type,s.scope_type))))}),(D==null?void 0:D.length)===0&&(E==null?void 0:E.appId)&&n.default.createElement(d.default.Option,{value:E.appId},n.default.createElement("div",{className:l.default.itemContent,title:E.appName},n.default.createElement("div",{className:l.default.appName},E.appName),n.default.createElement("div",{className:l.default.scopeTip},_e(E.osType,E.scopeType)))),Z&&n.default.createElement(d.default.Option,{value:"more",key:"more"},n.default.createElement("div",{style:{color:"#0064C8"}},n.default.createElement(t.default,{size:"small",type:"loading"}),"\xA0\xA0",n.default.createElement(y.default,null,"Load more options...")))),n.default.createElement("div",{id:"selectScroll"}))},ae=I;T.default=ae})},9644:function(N,m,e){var c,R,A,v=e(24596),p=e(67394),h=e(93168),Y=e(23587);(function(J,z){if(!0)!(R=[m,e(12955),e(17534),e(77809),e(81853),e(27378),e(66697),e(36012),e(14798),e(68055),e(34480),e(14870)],c=z,A=typeof c=="function"?c.apply(m,R):c,A!==void 0&&(N.exports=A));else var H})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(J,z,H,te,T,t,d,w,L,Q,a,n){"use strict";var y=e(67971);p(J,"__esModule",{value:!0}),J.default=void 0,z=y(z),H=y(H),te=y(te),T=y(T),t=ne(t),d=y(d),w=y(w),L=y(L),Q=y(Q),a=y(a);function $(l){if(typeof h!="function")return null;var r=new h,g=new h;return($=function(K){return K?g:r})(l)}function ne(l,r){if(!r&&l&&l.__esModule)return l;if(l===null||v(l)!=="object"&&typeof l!="function")return{default:l};var g=$(r);if(g&&g.has(l))return g.get(l);var C={},K=p&&Y;for(var U in l)if(U!=="default"&&Object.prototype.hasOwnProperty.call(l,U)){var I=K?Y(l,U):null;I&&(I.get||I.set)?p(C,U,I):C[U]=l[U]}return C.default=l,g&&g.set(l,C),C}var ie=function(r){var g=r.pluginType,C=r.isUninstall,K=r.configurationId,U=r.onClose,I=r.isInstall,ae=r.isClusterUninstall,o=r.ostype,f=(0,n.useDispatch)(),u=(0,t.useState)(""),_=(0,T.default)(u,2),E=_[0],S=_[1],W=(0,t.useState)(""),ue=(0,T.default)(W,2),G=ue[0],de=ue[1],D=(0,t.useCallback)((0,te.default)(regeneratorRuntime.mark(function X(){var O,Z,V,P;return regeneratorRuntime.wrap(function(F){for(;;)switch(F.prev=F.next){case 0:if(Z={Mode:"host",OsType:o},C?O="QueryUninstallCommand":O="QueryInstallCommand",I||(Z.ConfigurationId=K),!ae){F.next=7;break}S("helm delete --purge agent"),F.next=14;break;case 7:return F.next=9,f.agentSetting.getQueryUninstallAndInstallCommand(O,Z);case 9:V=F.sent,P=V.Data,P&&P.command_install&&S(P&&P.command_install),P&&P.command_uninstall&&S(P&&P.command_uninstall),P&&P.command_file_download&&de(P&&P.command_file_download);case 14:case"end":return F.stop()}},X)})),[g,C,K,ae]);(0,t.useEffect)(function(){((g==null?void 0:g.toUpperCase())==="CHAOS_AGENT"||I)&&D()},[D]);function k(){if(!C)return t.default.createElement("div",null,t.default.createElement("div",{className:a.default.title},t.default.createElement(d.default,null,"Before. If probes were installed")),t.default.createElement("div",{className:a.default.title},"1. ",t.default.createElement(d.default,null,"Download")),t.default.createElement("div",{className:a.default.item},t.default.createElement(d.default,null,"Download link:")," ",t.default.createElement("a",{href:G,download:!0,target:"_blank"},G)),t.default.createElement("div",{className:a.default.title},"2. ",t.default.createElement(d.default,null,"Decompress")),t.default.createElement("div",{className:a.default.title},"3. ",t.default.createElement(d.default,null,"Start the probe")),t.default.createElement("div",{className:a.default.code},E),t.default.createElement("div",{className:a.default.title},t.default.createElement(d.default,null,"Parameter Description"),":"),t.default.createElement("div",{className:a.default.item},"\xB7 appInstance\uFF1A",t.default.createElement(d.default,null,"Application name, you can customize")),t.default.createElement("div",{className:a.default.item},"\xB7 appGroup\uFF1A ",t.default.createElement(d.default,null,"Application group name, you can customize it")),t.default.createElement("div",{className:a.default.code},t.default.createElement("div",{className:a.default.command},t.default.createElement("span",null,"5928 RDP-Tcp#16"),t.default.createElement("span",null,"2\u2003\u2003\u200323,428 K"))));if(I||C&&!ae)return t.default.createElement("div",{style:{position:"relative"}},t.default.createElement("div",{className:a.default.copyBtn},t.default.createElement("a",{onClick:function(){return q(E)}},t.default.createElement(d.default,null,"Click to copy"))),t.default.createElement("div",{style:{paddingTop:20}},E));var X=`blades=($(kubectl get blade | grep -v NAME | awk '{print $1}' | tr '
' ' ')) && kubectl patch blade $blades --type merge -p '{"metadata":{"finalizers":[]}}'`;return t.default.createElement("div",null,t.default.createElement("div",{className:a.default.item},"1. ",t.default.createElement(d.default,null,"Execute the following Helm command to uninstall the probe")),x("helm un agent -n chaosblade",!0),t.default.createElement("div",{className:a.default.item},"2. ",t.default.createElement(d.default,null,"After the uninstallation is complete, execute the following command to check whether the probe pod in the command space has been uninstalled")),x("kubectl get pods -n chaosblade",!0),t.default.createElement("div",{className:a.default.item},"3. ",t.default.createElement(d.default,null,"If the uninstallation is abnormal, after ensuring that all the drills have been terminated, execute the following command to delete the drill in the abnormal state")),x(X,!0),t.default.createElement("div",{className:a.default.item},"4. ",t.default.createElement(d.default,null,"After execution, execute the following command to confirm that all chaosblade resources are deleted"),t.default.createElement("a",{href:G,download:!0,target:"_blank"},G)),x("kubectl get chaosblade",!0))}var x=function(O,Z){return t.default.createElement("div",{className:a.default.code},Z&&t.default.createElement("div",{className:a.default.copyBtn},t.default.createElement("a",{onClick:function(){return q(O)}},t.default.createElement(d.default,null,"Click to copy"))),O)},q=function(O){(0,w.default)(O),H.default.success(L.default.t("Copy successfully"))};return t.default.createElement(z.default,{visible:!0,title:C?L.default.t("Manually uninstall the plug-in").toString():L.default.t("Manually install the plug-in on a single machine").toString(),footerActions:["cancel"],style:{minWidth:"600px"},onClose:U,onCancel:U,shouldUpdatePosition:!0,locale:(0,Q.default)().Dialog},t.default.createElement("div",{className:a.default.content},k()))},le=(0,t.memo)(ie);J.default=le})},78576:(N,m,e)=>{"use strict";e.d(m,{Z:()=>h});var c=e(60994),R=e.n(c),A=e(93476),v=e.n(A),p=v()(R());p.push([N.id,`.index__itemContent__z8YF9 {
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
}`],sourceRoot:""}]),p.locals={itemContent:"index__itemContent__z8YF9",scopeTip:"index__scopeTip__JF-e2",appName:"index__appName__CfdNm",appSelect:"index__appSelect__cjp84"};const h=p},91814:(N,m,e)=>{"use strict";e.d(m,{Z:()=>h});var c=e(60994),R=e.n(c),A=e(93476),v=e.n(A),p=v()(R());p.push([N.id,`.index__content__ChFWv {
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
}`],sourceRoot:""}]),p.locals={content:"index__content__ChFWv",title:"index__title__nSbjq",item:"index__item__A8j2M",code:"index__code__ezF32",copyBtn:"index__copyBtn__usM0e",command:"index__command__bQlmQ"};const h=p},74427:(N,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>h});var c=e(1892),R=e.n(c),A=e(78576),v={};v.insert="head",v.singleton=!1;var p=R()(A.Z,v);const h=A.Z.locals||{}},34480:(N,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>h});var c=e(1892),R=e.n(c),A=e(91814),v={};v.insert="head",v.singleton=!1;var p=R()(A.Z,v);const h=A.Z.locals||{}}}]);

//# sourceMappingURL=970.bundle.js.map