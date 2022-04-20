(self.webpackChunk=self.webpackChunk||[]).push([[313],{17047:function(z,f,e){var r,B,s,g=e(67394);(function(o,A){if(!0)!(B=[f],r=A,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o){"use strict";g(o,"__esModule",{value:!0}),o.TASK=o.RECOVER=o.OBSERVER=o.NodeRunResult=o.NORMAL=o.ExperimentConstants=void 0;var A={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:"\u5F02\u5E38",FAILED:"\u4E0D\u7B26\u5408\u9884\u671F",STOPPED:"\u4E2D\u65AD",SUCCESS:"\u6210\u529F",EXCEPTION:"\u5F02\u5E38",TOTAL:"\u6F14\u7EC3\u6B21\u6570"};o.ExperimentConstants=A;var F={REJECTED:"REJECTED",SUCCESS:"SUCCESS",FAILED:["FAILED","SOPPED_FAILED","ERROR","STOPPED"]};o.NodeRunResult=F;var y=-1;o.NORMAL=y;var c=0;o.OBSERVER=c;var C=1;o.RECOVER=C;var se=2;o.TASK=se})},96042:function(z,f,e){var r,B,s,g=e(24596),o=e(67394),A=e(93168),F=e(23587),y=e(41281),c=e(50093),C=e(59396),se=e(75453);(function(M,h){if(!0)!(B=[f,e(73220),e(12955),e(93484),e(94188),e(17225),e(72153),e(77809),e(81853),e(85645),e(27378),e(98784),e(54581),e(73262),e(14870)],r=h,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var n})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(M,h,n,b,re,i,u,k,P,ue,l,ee,m,Pe,d){"use strict";var a=e(67971);o(M,"__esModule",{value:!0}),M.default=void 0,h=a(h),n=a(n),b=a(b),re=a(re),i=a(i),u=a(u),k=a(k),P=a(P),ue=a(ue),l=D(l),ee=a(ee),m=a(m);function U(t){if(typeof A!="function")return null;var I=new A,E=new A;return(U=function(j){return j?E:I})(t)}function D(t,I){if(!I&&t&&t.__esModule)return t;if(t===null||g(t)!=="object"&&typeof t!="function")return{default:t};var E=U(I);if(E&&E.has(t))return E.get(t);var H={},j=o&&F;for(var X in t)if(X!=="default"&&Object.prototype.hasOwnProperty.call(t,X)){var Ee=j?F(t,X):null;Ee&&(Ee.get||Ee.set)?o(H,X,Ee):H[X]=t[X]}return H.default=t,E&&E.set(t,H),H}function de(t,I){var E=typeof y!="undefined"&&t[c]||t["@@iterator"];if(!E){if(C(t)||(E=ne(t))||I&&t&&typeof t.length=="number"){E&&(t=E);var H=0,j=function(){};return{s:j,n:function(){return H>=t.length?{done:!0}:{done:!1,value:t[H++]}},e:function($){throw $},f:j}}throw new TypeError(`Invalid attempt to iterate non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}var X=!0,Ee=!1,Ce;return{s:function(){E=E.call(t)},n:function(){var $=E.next();return X=$.done,$},e:function($){Ee=!0,Ce=$},f:function(){try{!X&&E.return!=null&&E.return()}finally{if(Ee)throw Ce}}}}function ne(t,I){if(!t)return;if(typeof t=="string")return Z(t,I);var E=Object.prototype.toString.call(t).slice(8,-1);if(E==="Object"&&t.constructor&&(E=t.constructor.name),E==="Map"||E==="Set")return se(t);if(E==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(E))return Z(t,I)}function Z(t,I){(I==null||I>t.length)&&(I=t.length);for(var E=0,H=new Array(I);E<I;E++)H[E]=t[E];return H}var pe=ue.default.SubMenu,Me=ue.default.Item;function Ie(t){var I=(0,d.useDispatch)(),E=(0,l.useState)([]),H=(0,P.default)(E,2),j=H[0],X=H[1],Ee=(0,l.useState)(""),Ce=(0,P.default)(Ee,2),te=Ce[0],$=Ce[1],ae=(0,l.useState)(1),R=(0,P.default)(ae,2),me=R[0],Be=R[1],Oe=(0,l.useState)(1),Se=(0,P.default)(Oe,2),ge=Se[0],Ue=Se[1],Le=(0,l.useState)(1),De=(0,P.default)(Le,2),Ne=De[0],G=De[1],ie=(0,l.useState)(null),We=(0,P.default)(ie,2),_e=We[0],_=We[1],v=(0,l.useState)([]),q=(0,P.default)(v,2),K=q[0],O=q[1],V=(0,l.useState)(""),xe=(0,P.default)(V,2),Re=xe[0],ce=xe[1],ve=(0,l.useState)([]),fe=(0,P.default)(ve,2),L=fe[0],T=fe[1],Q=(0,l.useState)(!1),le=(0,P.default)(Q,2),Ae=le[0],we=le[1],Fe=(0,l.useState)(!1),ze=(0,P.default)(Fe,2),je=ze[0],Xe=ze[1];(0,l.useEffect)(function(){t.visible||(X([]),$(""),Be(1),_(null),O([]),ce(""),T([]),we(!1))},[t.visible]),(0,l.useEffect)(function(){if(Be(1),X([]),$(""),O([]),_(null),T([]),Ae){var x=t.phase,S=t.scopeType,W=t.k8sResourceType;(0,k.default)(regeneratorRuntime.mark(function Y(){return regeneratorRuntime.wrap(function(J){for(;;)switch(J.prev=J.next){case 0:return J.next=2,I.experimentScene.searchFunctions({key:Re,phase:x,page:1,scopeType:S,k8sResourceType:W},function(p){var w=p.data,N=w===void 0?[]:w,he=p.pageSize,Te=he===void 0?1:he,ke=p.total,ye=ke===void 0?1:ke;G(ye),Ue(Te),O(N)});case 2:case"end":return J.stop()}},Y)}))()}else ce("")},[Ae,je]),(0,l.useEffect)(function(){if(!t.visible)return;if(!Ae){var x=t.phase,S=t.scopeType,W=t.nodeType,Y=t.cloudServiceType,oe=t.osType;W===Pe.NODE_TYPE.OBSERVER?(0,k.default)(regeneratorRuntime.mark(function J(){return regeneratorRuntime.wrap(function(w){for(;;)switch(w.prev=w.next){case 0:return w.next=2,I.experimentScene.getGlobalCategories(function(N){ee.default.isEqual(j,N)||X(N)});case 2:case"end":return w.stop()}},J)}))():W===Pe.NODE_TYPE.RECOVER?(0,k.default)(regeneratorRuntime.mark(function J(){return regeneratorRuntime.wrap(function(w){for(;;)switch(w.prev=w.next){case 0:return w.next=2,I.experimentScene.getGuardCategories(function(N){ee.default.isEqual(j,N)||X(N)});case 2:case"end":return w.stop()}},J)}))():(0,k.default)(regeneratorRuntime.mark(function J(){var p;return regeneratorRuntime.wrap(function(N){for(;;)switch(N.prev=N.next){case 0:return N.next=2,I.experimentScene.getCategories({phase:x,scopeType:S,filterNoChild:!0,cloudServiceType:Y,osType:oe});case 2:p=N.sent,ee.default.isEqual(j,p)||X(p);case 4:case"end":return N.stop()}},J)}))()}},[t.visible,t.phase,t.scopeType,t.nodeType,Ae,t.osType]),(0,l.useEffect)(function(){if(j.length>0){for(var x=j[0];x.children&&x.children.length>0;)x=x.children[0];$(x.categoryId)}},[j]),(0,l.useEffect)(function(){Be(1);var x=[];S(j,x),x.length>0?T([].concat(x)):j.length>0&&T([j[0].categoryId]);function S(W,Y){var oe=de(W),J;try{for(oe.s();!(J=oe.n()).done;){var p=J.value;if(p.children&&p.children.length>0){if(Y.push(p.categoryId),S(p.children,Y))return!0;Y.pop()}else{if(p.categoryId!==te)continue;return!0}}}catch(w){oe.e(w)}finally{oe.f()}return!1}},[te]),(0,l.useEffect)(function(){if(!t.visible)return;var x=t.phase,S=t.scopeType,W=t.k8sResourceType,Y=t.osType;Ae?(0,k.default)(regeneratorRuntime.mark(function oe(){return regeneratorRuntime.wrap(function(p){for(;;)switch(p.prev=p.next){case 0:return p.next=2,I.experimentScene.searchFunctions({key:Re,phase:x,page:me,scopeType:S,k8sResourceType:W,osType:Y},function(w){var N=w.data,he=N===void 0?[]:N,Te=w.total,ke=Te===void 0?1:Te,ye=w.pageSize,Ye=ye===void 0?1:ye;G(ke),Ue(Ye),ee.default.isEqual(K,he)||O(he)});case 2:case"end":return p.stop()}},oe)}))():te&&me&&(0,k.default)(regeneratorRuntime.mark(function oe(){return regeneratorRuntime.wrap(function(p){for(;;)switch(p.prev=p.next){case 0:return p.next=2,I.experimentScene.getFunctionsByCategoryId({page:me,categoryId:te,phase:x,scopeType:S,k8sResourceType:W,size:12,osType:Y},function(w){if(w){var N=w.data,he=w.total,Te=w.pageSize;G(he),Ue(Te),ee.default.isEqual(K,N)||O(N)}});case 2:case"end":return p.stop()}},oe)}))()},[t.phase,t.scopeType,te,me,t.k8sResourceType,t.osType]),(0,l.useEffect)(function(){K.length>0?_(K[0]):_(null)},[K]);var Ke=function(){t.onClose()},Ve=function(S){return ce(S)},Ze=function(){we(!0),Xe(!je)},He=function(){we(!Ae)},$e=function(S){S!==te&&$(S)},Ge=function(S){var W=K.filter(function(Y){return Y.functionId===S});W.length>0&&_(W[0])},Qe=function(S){Be(S)},Je=function(S){return l.default.createElement(ue.default,{className:m.default.categoryList,selectMode:"single",defaultOpenKeys:L,selectedKeys:te,hasSelectedIcon:!1,onItemClick:$e,inlineIndent:10},qe(S))},qe=function x(S){var W=[],Y=de(S),oe;try{for(Y.s();!(oe=Y.n()).done;){var J=oe.value,p=J.categoryId,w=J.name,N=J.children;N&&N.length>0?W.push(l.default.createElement(pe,{key:p,label:w},x(N))):W.push(l.default.createElement(Me,{key:p},w))}}catch(he){Y.e(he)}finally{Y.f()}return W},en=function(){var x=(0,k.default)(regeneratorRuntime.mark(function S(){var W,Y,oe;return regeneratorRuntime.wrap(function(p){for(;;)switch(p.prev=p.next){case 0:if(W=t.phase,!(W&&_e&&!_e.arguments||W&&!_e)){p.next=6;break}return p.next=4,new h.default(function(w,N){var he=ee.default.get(_e,"functionId","");(0,k.default)(regeneratorRuntime.mark(function Te(){return regeneratorRuntime.wrap(function(ye){for(;;)switch(ye.prev=ye.next){case 0:return ye.next=2,I.experimentScene.getFunctionParameters({functionId:he},function(Ye,nn){Ye?N(Ye):w(nn)});case 2:case"end":return ye.stop()}},Te)}))()});case 4:Y=p.sent,ee.default.set(_e,"arguments",Y);case 6:oe=t.onSelect,oe(ee.default.cloneDeep(_e)),Ke();case 9:case"end":return p.stop()}},S)}));return function(){return x.apply(this,arguments)}}();return l.default.createElement(n.default,{title:t.title,visible:t.visible,footer:l.default.createElement("div",{className:m.default.btnRow},l.default.createElement(u.default,{className:m.default.btn,type:"primary",disabled:K.length===0,onClick:en,"data-autolog":"text=\u786E\u8BA4\u9009\u62E9\u5C0F\u7A0B\u5E8F"},"\u786E\u5B9A"),l.default.createElement(u.default,{className:m.default.btn,type:"normal",onClick:Ke,"data-autolog":"\u53D6\u6D88\u9009\u62E9\u5C0F\u7A0B\u5E8F"},"\u53D6\u6D88")),onCancel:Ke,onClose:Ke,style:{minWidth:968}},l.default.createElement("div",{className:m.default.container},t.searchable?l.default.createElement("div",{className:m.default.searchBox},l.default.createElement(i.default,{type:"search",size:"xs",className:m.default.icon}),l.default.createElement(re.default,{className:m.default.search,value:Re,onChange:Ve,onSearch:Ze,placeholder:"\u641C\u7D22\u6545\u969C\u6807\u9898",searchText:"\u641C\u7D22",hasIcon:!1})):null,l.default.createElement("div",{className:m.default.contentBox},l.default.createElement("div",{className:m.default.categoryBox},!Ae&&L.length>0&&Je(j),Ae&&l.default.createElement("p",null,l.default.createElement("span",null,"\u641C\u7D22\u7ED3\u679C\uFF1A"),l.default.createElement("span",{className:m.default.clearSearch,onClick:He},"\u6E05\u7A7A"))),l.default.createElement("div",{className:m.default.listBox},l.default.createElement("ul",{className:m.default.functionList},ee.default.isEmpty(K)?l.default.createElement("div",null,"\u5F53\u524D\u5206\u7C7B\u6682\u65E0\u573A\u666F"):K.map(function(x){var S=x.name,W=x.functionId,Y=!1;return _e&&(_e.functionId===W&&(Y=!0)),l.default.createElement("li",{className:Y?m.default.selectedFunc:void 0,key:W,onClick:function(){return Ge(W)},"data-autolog":"text=\u70B9\u51FB\u5C0F\u7A0B\u5E8F"},S)})),l.default.createElement(b.default,{shape:"arrow-only",current:me,total:Ne,pageSize:ge,totalRender:function(){return"\u5171\u6709".concat(Ne,"\u6761")},hideOnlyOnePage:!0,onChange:function(S){return Qe(S)}}),_e?l.default.createElement("div",{className:m.default.descriptionBox},l.default.createElement("p",null,_e.description)):l.default.createElement("div",{style:{minHeight:220}})))))}var be=Ie;M.default=be})},63835:function(z,f,e){var r,B,s,g=e(67394);(function(o,A){if(!0)!(B=[f,e(93080),e(59607),e(32186),e(27378)],r=A,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,A,F,y,c){"use strict";var C=e(67971);g(o,"__esModule",{value:!0}),o.default=void 0,A=C(A),F=C(F),y=C(y),c=C(c);var se=["type","children"],M=new y.default,h=function(i){var u=i.scriptUrl,k=i.extraCommonProps,P=k===void 0?{}:k;if(typeof document!="undefined"&&typeof window!="undefined"&&typeof document.createElement=="function"&&typeof u=="string"&&u.length&&!M.has(u)){var ue=document.createElement("script");ue.setAttribute("src",u),ue.setAttribute("data-namespace",u),M.add(u),document.body.appendChild(ue)}var l=function(m){var Pe=m.type,d=m.children,a=(0,F.default)(m,se),U=null;return m.type&&(U=c.default.createElement("use",{xlinkHref:"#".concat(Pe)})),d&&(U=d),c.default.createElement("svg",(0,A.default)({},a,P),U)};return l.displayName="Iconfont",l},n=h({scriptUrl:"//at.alicdn.com/t/font_976326_mtiq05ajtqs.js"}),b=n;o.default=b})},76313:function(z,f,e){var r,B,s,g=e(24596),o=e(67394),A=e(93168),F=e(23587);(function(y,c){if(!0)!(B=[f,e(17225),e(81853),e(96042),e(9577),e(27378),e(89724),e(98784),e(39725)],r=c,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(y,c,C,se,M,h,n,b,re){"use strict";var i=e(67971);o(y,"__esModule",{value:!0}),y.default=void 0,c=i(c),C=i(C),se=i(se),M=i(M),h=k(h),n=i(n),b=i(b),re=i(re);function u(a){if(typeof A!="function")return null;var U=new A,D=new A;return(u=function(ne){return ne?D:U})(a)}function k(a,U){if(!U&&a&&a.__esModule)return a;if(a===null||g(a)!=="object"&&typeof a!="function")return{default:a};var D=u(U);if(D&&D.has(a))return D.get(a);var de={},ne=o&&F;for(var Z in a)if(Z!=="default"&&Object.prototype.hasOwnProperty.call(a,Z)){var pe=ne?F(a,Z):null;pe&&(pe.get||pe.set)?o(de,Z,pe):de[Z]=a[Z]}return de.default=a,D&&D.set(a,de),de}var P=16,ue=16,l=244,ee=0,m=0;function Pe(a){var U=(0,h.useRef)(null),D;a.editable?m=48:m=29,a.deletable?ee=32:ee=0;var de=(0,h.useState)(0),ne=(0,C.default)(de,2),Z=ne[0],pe=ne[1],Me=(0,h.useState)(NaN),Ie=(0,C.default)(Me,2),be=Ie[0],t=Ie[1],I=(0,h.useState)(),E=(0,C.default)(I,2),H=E[0],j=E[1],X=(0,h.useState)(),Ee=(0,C.default)(X,2),Ce=Ee[0],te=Ee[1],$=(0,h.useState)(!1),ae=(0,C.default)($,2),R=ae[0],me=ae[1];(0,h.useEffect)(function(){Be(),Oe()},[]),(0,h.useEffect)(function(){return function(){D&&clearTimeout(D)}},[]);function Be(){var _=a.editable,v=U.current&&U.current.scrollWidth-(_?P*2:0);v!==Z&&pe(v)}function Oe(){var _=1e3;D&&clearTimeout(D),D=setTimeout(function(){Be(),Oe()},_)}var Se=function(v,q){var K=a.onNodeAdding;K&&K();var O;v?v.phase===1<<1?q?q.phase===1<<3?O=1<<2|1<<3:q.phase&&(O=q.phase):O=1<<2|1<<3:v.phase&&(O=v.phase):O=1<<0,j(v),te(q),t(O),me(!0)};function ge(){me(!1)}function Ue(_){var v;switch(be){case 1<<0:v="prepare";break;case 1<<1:v="attack";break;case 1<<2:v="check";break;case 1<<3:v="recover";break;default:v=void 0;break}if(!v){var q=b.default.get(_,"phaseFlag");q&&((q&8)===8?v="recover":v="check")}_.stage=v,H?H.insertAfter(_):Ce&&Ce.insertBefore(_);var K=a.onNodeAdd;K&&K(_)}function Le(_,v){var q=a.editable,K=_-ee,O=q?ue+l+m:l+m,V=0;if(K>=O){V=1;for(var xe=K-O;xe>=m+l;)++V,xe-=m+l;xe>l&&v>V+1&&++V}return V}function De(_){var v=a.editable,q=a.selectedNode,K=a.isExpertise,O=a.running,V=Le(Z,_.length),xe=l/2+(v?ue:0),Re=(l+m)*(V-1),ce=[];return Z>0&&V>0&&b.default.forEach(_,function(ve,fe){var L=ve.id,T=ve.activityId,Q=q&&ve.id===q.id,le=fe===0,Ae=fe!==0&&(fe+1)%V===1,we=fe!==_.length-1&&(fe+1)%V===0,Fe=fe===_.length-1;ce.push(h.default.createElement(M.default,{key:L||T,data:ve,editable:v,isExpertise:K,selected:Q,isHead:le,isLineFirst:Ae,isLineLast:we,isLast:Fe,onClick:a.onNodeClick,onNodeAddClick:Se,onNodeDeleteClick:a.onNodeDelete,onTryAgainClick:a.onTryAgain,running:O,onCheck:a.onCheck,permission:a.permission})),we&&ce.push(h.default.createElement(n.default,{key:fe,data:ve,editable:v,lineWidth:Re,padding:xe,onNodeAddClick:Se}))}),ce}var Ne=a.editable,G=a.nodes,ie=a.scopeType,We=a.deletable,_e=Ne?re.default.boxContainer:re.default.boxContainerReadOnly;return b.default.isEmpty(G)?null:h.default.createElement("div",{className:_e},h.default.createElement("div",{ref:U,className:re.default.box},De(G)),We&&h.default.createElement("div",{className:re.default.deleteFlowBox,onClick:a.onDelete,"data-autolog":"text=\u5220\u9664\u6F14\u7EC3\u5206\u7EC4"},h.default.createElement(c.default,{type:"ashbin",size:"small",style:{color:"#888"},"data-autolog":"text=\u5220\u9664\u6F14\u7EC3\u5206\u7EC4"})),h.default.createElement(se.default,{title:"\u9009\u62E9\u6F14\u7EC3\u8282\u70B9",searchable:!0,visible:R,phase:be,scopeType:ie,onClose:ge,onSelect:Ue}))}var d=Pe;y.default=d})},89724:function(z,f,e){var r,B,s,g=e(67394);(function(o,A){if(!0)!(B=[f,e(17225),e(63835),e(27378),e(1918)],r=A,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,A,F,y,c){"use strict";var C=e(67971);g(o,"__esModule",{value:!0}),o.default=se,A=C(A),F=C(F),y=C(y),c=C(c);function se(M){function h(u,k){var P=M.onNodeAddClick;P&&P(u,k)}var n=M.data,b=M.editable,re=M.lineWidth,i=M.padding;return y.default.createElement("div",{className:c.default.switchArrowContainer,style:{width:"100%",paddingLeft:i}},y.default.createElement("div",{className:c.default.switchArrowDownContainer},y.default.createElement("div",{className:c.default.switchArrowDown}),y.default.createElement(A.default,{className:c.default.switchArrowDownIcon,type:"caret-down",size:"small"})),y.default.createElement("div",{className:c.default.switchArrowLine,style:{width:re}}),y.default.createElement("div",{className:c.default.switchArrowUpContainer},y.default.createElement("div",{className:c.default.switchArrowUp}),b&&y.default.createElement(F.default,{className:c.default.switchArrowIcon,type:"icon-tianjia2",onClick:function(){return h(n,n.next)}})))}})},9577:function(z,f,e){var r,B,s,g=e(24596),o=e(67394),A=e(93168),F=e(23587);(function(y,c){if(!0)!(B=[f,e(72153),e(17225),e(57379),e(81853),e(63835),e(27378),e(98784),e(60042),e(1918),e(17047),e(17640)],r=c,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(y,c,C,se,M,h,n,b,re,i,u,k){"use strict";var P=e(67971);o(y,"__esModule",{value:!0}),y.default=Pe,c=P(c),C=P(C),se=P(se),M=P(M),h=P(h),n=l(n),b=P(b),re=P(re),i=P(i);function ue(d){if(typeof A!="function")return null;var a=new A,U=new A;return(ue=function(de){return de?U:a})(d)}function l(d,a){if(!a&&d&&d.__esModule)return d;if(d===null||g(d)!=="object"&&typeof d!="function")return{default:d};var U=ue(a);if(U&&U.has(d))return U.get(d);var D={},de=o&&F;for(var ne in d)if(ne!=="default"&&Object.prototype.hasOwnProperty.call(d,ne)){var Z=de?F(d,ne):null;Z&&(Z.get||Z.set)?o(D,ne,Z):D[ne]=d[ne]}return D.default=d,U&&U.set(d,D),D}var ee={prepare:"#FFDDB2",attack:"#79B3F3",check:"#BAB1EA",recover:"#2A828A"},m={0:{tag:"#EDF7ED",word:"#629962"},1:{tag:"#EDF3F7",word:"#628099"},2:{tag:"#F0EDF7",word:"#746299"},3:{tag:"#F7EDED",word:"#996262"},4:{tag:"#F7F6ED",word:"#999062"}};function Pe(d){var a=d.permission,U=(0,n.useState)(!1),D=(0,M.default)(U,2),de=D[0],ne=D[1],Z=(0,n.useState)(!1),pe=(0,M.default)(Z,2),Me=pe[0],Ie=pe[1],be=(0,n.useState)(!1),t=(0,M.default)(be,2),I=t[0],E=t[1],H=function(){var T=d.data,Q=d.onClick;b.default.isEmpty(T)||Q&&Q(T)},j=function(T){T.stopPropagation();var Q=d.data,le=d.onNodeDeleteClick;le&&le(Q)},X=function(T,Q){var le=d.onNodeAddClick;le&&le(T,Q)},Ee=function(T){if(E(!0),T.stopPropagation(),!(0,k.handleIsAdmin)(a,4))return;var Q=d.data,le=d.onTryAgainClick;b.default.isEmpty(Q)||le&&le(Q,function(){E(!1)})},Ce=function(T,Q){if(Q?ne(!0):Ie(!0),T.stopPropagation(),!(0,k.handleIsAdmin)(a,4))return;var le=d.data,Ae=d.onCheck;b.default.isEmpty(le)||Ae&&Ae(Q,le,function(){Q?ne(!1):Ie(!1)})};function te(){var L=d.data,T=!1;if(!b.default.isEmpty(L)){var Q=L.arguments||L.args;b.default.isEmpty(Q)||Q.forEach(function(le){var Ae;(Ae=le.argumentList)===null||Ae===void 0||Ae.forEach(function(we){var Fe=we.component;b.default.isEmpty(Fe)||Fe.required&&(T=!0)})})}return T}var $=d.isExpertise,ae=d.editable,R=d.data,me=d.isHead,Be=d.isLineFirst,Oe=d.isLineLast,Se=d.isLast,ge=d.selected,Ue=R.stage,Le=R.activityName,De=R.name,Ne=R.miniAppName,G=R.state,ie=R.runResult,We=R.retryable,_e=R.userCheckState,_=R.nodeType,v=R.hostPercent,q=R.hosts,K=Le||De||Ne,O=te(),V=(0,se.default)({},i.default.nodeBox,!0),xe=d.hasOwnProperty("deletable")?d.deletable:R.deletable,Re,ce;_!==u.NORMAL&&_!==u.TASK?(ce=null,Re=i.default.globalNodeContainer):(ce=ae?i.default.tailBox:i.default.tailBoxReadOnly,Re=i.default.normalNodeContainer),ge&&(V[i.default.selectedNode]=!0),R.hasOwnProperty("argsValid")&&(ae&&!R.argsValid&&!$&&(V[i.default.selectedNode]=!1,V[i.default.errorNode]=!0)),G==="RUNNING"&&!ge?V[i.default.activityRunning]=!0:G==="FINISHED"&&!ge&&(ie&&ie===u.NodeRunResult.SUCCESS?V[i.default.activitySuccess]=!0:ie&&ie===u.NodeRunResult.REJECTED?V[i.default.activityRejected]=!0:V[i.default.activityFailed]=!0);function ve(L){return m[L%5].tag||""}function fe(L){return m[L%5].word||""}return n.default.createElement("div",{className:Re},ae&&me&&n.default.createElement("div",{className:i.default.headBox},n.default.createElement(h.default,{className:i.default.iconHead,type:"icon-tianjia2",onClick:function(){return X(null,R)},"data-autolog":"text=\u6DFB\u52A0\u6F14\u7EC3\u8282\u70B9"}),n.default.createElement("div",{className:i.default.arrowLineHead})),Be&&ae&&n.default.createElement("div",{className:i.default.firstBox}),n.default.createElement("div",{title:Le||De||Ne,className:(0,re.default)(V),onClick:H},n.default.createElement("div",{className:i.default.topLayer},n.default.createElement("div",{className:i.default.topLeftBox},n.default.createElement("div",{className:i.default.stageIcon,style:{backgroundColor:ee[Ue]}}),n.default.createElement("span",{title:K,className:i.default.nodeName},K)),n.default.createElement("div",{className:i.default.topRightBox},ae&&!$&&R.argsValid===!1&&n.default.createElement(C.default,{type:"delete-filling",size:"xs",style:{color:"#D93026",marginRight:12}}),ie&&ie===u.NodeRunResult.SUCCESS&&n.default.createElement(C.default,{type:"success-filling",size:"small",style:{color:"#1E8E3E",marginRight:12}}),ie&&b.default.indexOf(u.NodeRunResult.FAILED,ie)>=0&&n.default.createElement(C.default,{type:"delete-filling",size:"small",style:{color:"#D93026",marginRight:12}}),G==="RUNNING"&&n.default.createElement(C.default,{type:"loading",size:"small",style:{marginRight:12}}),!ae&&R.groupOrder&&n.default.createElement("div",{className:i.default.groupOrderBox,style:{backgroundColor:ve(R.groupOrder)}},n.default.createElement("span",{style:{color:fe(R.groupOrder)}},"\u5206\u7EC4",R.groupOrder)))),n.default.createElement("div",{className:i.default.bottomLayer},n.default.createElement("div",{className:i.default.bottomLeftBox},n.default.createElement("div",{className:i.default.parameterInfo},!$&&ae&&!G&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,"\u5FC5\u586B\u53C2\u6570\uFF1A"),(O||_===u.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},"\u6709"),!O&&_!==u.RECOVER&&n.default.createElement("span",null,"\u65E0")),!$&&!ae&&!G&&_!==u.NORMAL&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,"\u5FC5\u586B\u53C2\u6570\uFF1A"),(O||_===u.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},"\u6709"),!O&&_!==u.RECOVER&&n.default.createElement("span",null,"\u65E0")),$&&!ae&&!G&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,"\u5FC5\u586B\u53C2\u6570\uFF1A"),(O||_===u.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},"\u6709"),!O&&_!==u.RECOVER&&n.default.createElement("span",null,"\u65E0")),!$&&!ae&&_===u.NORMAL&&!G&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,"\u6D89\u53CA\u673A\u5668\uFF1A"),v&&v!==0&&!q?n.default.createElement("span",{style:{color:"#0070cc"}},v,"%"):n.default.createElement("span",null),q&&n.default.createElement("span",{style:{color:"#0070cc"}},"".concat(R.hosts,"\u4E2A"))),_e===u.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING&&n.default.createElement("span",null,"\u5F85\u624B\u52A8\u63A8\u8FDB"),ie&&ie===u.NodeRunResult.SUCCESS&&n.default.createElement("span",null,"\u8282\u70B9\u6210\u529F\u6267\u884C"),ie&&b.default.indexOf(u.NodeRunResult.FAILED,ie)>=0&&n.default.createElement("span",null,"\u8282\u70B9\u6267\u884C\u5931\u8D25"),ie&&ie===u.NodeRunResult.REJECTED&&n.default.createElement("span",null,"\u8282\u70B9\u6267\u884C\u88AB\u8DF3\u8FC7"),G&&G==="RUNNING"&&n.default.createElement("span",null,"\u8282\u70B9\u6267\u884C\u4E2D\u2026"))),n.default.createElement("div",{className:i.default.bottomRightBox},n.default.createElement("div",{className:i.default.userAction},We&&n.default.createElement("span",{className:i.default.contiueTryIcon,onClick:function(T){return Ee(T)}},n.default.createElement(c.default,{type:"primary",loading:I,text:!0,className:i.default.action,disabled:!(0,k.handleIsAdmin)(a,4)},"\u91CD\u8BD5")),_e===u.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING?n.default.createElement("span",{className:i.default.userCheck},n.default.createElement("span",{className:i.default.contiueTryIcon,onClick:function(T){return Ce(T,!0)}},n.default.createElement(c.default,{type:"primary",loading:de,text:!0,className:i.default.action,disabled:!(0,k.handleIsAdmin)(a,4)},"\u7EE7\u7EED")),n.default.createElement("span",{onClick:function(T){return Ce(T,!1)}},n.default.createElement(c.default,{type:"primary",loading:Me,text:!0,className:i.default.action,disabled:!(0,k.handleIsAdmin)(a,4)},"\u7EC8\u6B62"))):n.default.createElement("span",null)))),G?ge&&n.default.createElement("div",{className:i.default.selectedTriangle}):ge&&R.argsValid&&n.default.createElement("div",{className:i.default.selectedTriangle}),ae&&xe&&!G&&n.default.createElement(C.default,{type:"ashbin",size:"small",style:{position:"absolute",top:16,right:12,color:"#888"},onClick:function(T){return j(T)},"data-autolog":"text=\u5220\u9664\u6F14\u7EC3\u8282\u70B9"}),G&&G==="RUNNING"&&n.default.createElement("div",null)),!Oe&&!Se&&n.default.createElement("div",{className:ce},_===u.NORMAL||_===u.TASK?n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:i.default.arrowLineTail}),n.default.createElement(C.default,{type:"caret-right",size:"small"})):null,_===u.NORMAL&&ae&&n.default.createElement(h.default,{className:i.default.iconTail,type:"icon-tianjia2",onClick:function(){return X(R,R.next)},"data-autolog":"text=\u6DFB\u52A0\u6F14\u7EC3\u8282\u70B9"})),Se&&ae&&n.default.createElement("div",{className:ce},_===u.NORMAL||_===u.TASK?n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:i.default.arrowLineTail}),n.default.createElement(C.default,{type:"caret-right",size:"small"})):null,n.default.createElement(h.default,{className:i.default.iconTail,type:"icon-tianjia2",onClick:function(){return X(R,R.next)},"data-autolog":"text=\u6DFB\u52A0\u6F14\u7EC3\u8282\u70B9"})))}})},17640:function(z,f,e){var r,B,s,g=e(67394);(function(o,A){if(!0)!(B=[f],r=A,s=typeof r=="function"?r.apply(f,B):r,s!==void 0&&(z.exports=s));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o){"use strict";g(o,"__esModule",{value:!0}),o.handleIsAdmin=void 0;var A=function(y,c){return(y&c)===c};o.handleIsAdmin=A})},94539:(z,f,e)=>{"use strict";e.d(f,{Z:()=>A});var r=e(60994),B=e.n(r),s=e(93476),g=e.n(s),o=g()(B());o.push([z.id,`.index__container__-R1ZY {
  max-width: 968px;
  overflow: hidden;
}

.index__searchBox__4itYE {
  position: relative;
  width: 950px;
  margin-bottom: 22px;
}

.index__searchBox__4itYE .index__icon__Yrchm {
  position: absolute;
  opacity: 0.5;
  color: #888;
  top: 8px;
  left: 17px;
}

.index__search__PJKPg {
  width: 100%;
}

.index__search__PJKPg .next-input input {
    padding-left: 33px;
  }

.index__search__PJKPg .next-input-group .next-input:hover {
    position: inherit !important;
  }

.index__search__PJKPg.next-search-btn {
  font-size: 12px !important;
}

.index__contentBox__lWDuC {
  /* margin-top: 22px; */
  height: 510px;
  /* max-height: 530px; */
  display: flex;
  flex-direction: row;
}

.index__categoryBox__PsQka {
  width: 160px;
  min-width: 160px;
  overflow-x: hidden;
  overflow-y: auto;
}

.index__categoryBox__PsQka .index__categoryList__8lpnx {
  height: 100%;
  padding-top: 0;
  border: none;
  border-right: 1px solid #dedede;
}

.index__categoryBox__PsQka .index__categoryList__8lpnx.next-selected {
  border-right: 2px solid #0070cc;
}

.index__categoryBox__PsQka .index__categoryList__8lpnx li.index__selectedCate__fNyLs {
  border-right: 2px solid #0070cc;
}

.index__clearSearch__GLS\\+S {
  margin-left: 10px;
  color: #0070cc;
  cursor: pointer;
}

.index__listBox__2s\\+St {
  position: relative;
  overflow: hidden;
  flex-grow: 1;
  padding-left: 4px;
}

.index__listBox__2s\\+St .index__functionList__8\\+lTI {
    list-style: none;
    overflow: hidden;
    min-height: 224px;
  }

.index__listBox__2s\\+St .index__functionList__8\\+lTI li {
    width: 257px;
    height: 40px;
    font-size: 12px;
    line-height: 40px;
    text-align: center;
    border: 1px solid #dedede;
    margin: 0 8px 16px 0;
    cursor: pointer;
    float: left;
  }

.index__listBox__2s\\+St .next-pagination {
    float: none;
    text-align: right;
    margin-bottom: 8px;
    margin-top: -8px;
    margin-right: 17px;
  }

.index__listBox__2s\\+St .index__functionList__8\\+lTI li.index__selectedFunc__SnSVV {
  color: #0070cc;
  border: 1px solid #0070cc;
}

.index__loading__IvmoD {
  width: 100%;
}

.index__descriptionBox__KVOZB {
  margin-right: 16px;
  padding: 5px;
  height: 220px;
  overflow-y: auto;
  font-size: 12px;
  color: #000;
  letter-spacing: 0;
  line-height: 18px;
  border: 1px solid #dedede;
}

.index__btnRow__f0\\+Nv {
  text-align: right;
}

.index__btn__5PaRI {
  margin-left: 8px;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/common/AddFunction/index.css"],names:[],mappings:"AAAA;EACE,gBAAgB;EAChB,gBAAgB;AAClB;;AAEA;EACE,kBAAkB;EAClB,YAAY;EACZ,mBAAmB;AACrB;;AAEA;EACE,kBAAkB;EAClB,YAAY;EACZ,WAAW;EACX,QAAQ;EACR,UAAU;AACZ;;AAEA;EACE,WAAW;AAOb;;AANE;IACE,kBAAkB;EACpB;;AACA;IACE,4BAA4B;EAC9B;;AAGF;EACE,0BAA0B;AAC5B;;AAEA;EACE,sBAAsB;EACtB,aAAa;EACb,uBAAuB;EACvB,aAAa;EACb,mBAAmB;AACrB;;AAEA;EACE,YAAY;EACZ,gBAAgB;EAChB,kBAAkB;EAClB,gBAAgB;AAClB;;AAEA;EACE,YAAY;EACZ,cAAc;EACd,YAAY;EACZ,+BAA+B;AACjC;;AAEA;EACE,+BAA+B;AACjC;;AAEA;EACE,+BAA+B;AACjC;;AAEA;EACE,iBAAiB;EACjB,cAAc;EACd,eAAe;AACjB;;AAEA;EACE,kBAAkB;EAClB,gBAAgB;EAChB,YAAY;EACZ,iBAAiB;AA2BnB;;AAzBE;IACE,gBAAgB;IAChB,gBAAgB;IAChB,iBAAiB;EACnB;;AAEA;IACE,YAAY;IACZ,YAAY;IACZ,eAAe;IACf,iBAAiB;IACjB,kBAAkB;IAClB,yBAAyB;IACzB,oBAAoB;IACpB,eAAe;IACf,WAAW;EACb;;AAEA;IACE,WAAW;IACX,iBAAiB;IACjB,kBAAkB;IAClB,gBAAgB;IAChB,kBAAkB;EACpB;;AAGF;EACE,cAAc;EACd,yBAAyB;AAC3B;;AAEA;EACE,WAAW;AACb;;AAEA;EACE,kBAAkB;EAClB,YAAY;EACZ,aAAa;EACb,gBAAgB;EAChB,eAAe;EACf,WAAW;EACX,iBAAiB;EACjB,iBAAiB;EACjB,yBAAyB;AAC3B;;AAEA;EACE,iBAAiB;AACnB;;AAEA;EACE,gBAAgB;AAClB",sourcesContent:[`.container {
  max-width: 968px;
  overflow: hidden;
}

.searchBox {
  position: relative;
  width: 950px;
  margin-bottom: 22px;
}

.searchBox .icon {
  position: absolute;
  opacity: 0.5;
  color: #888;
  top: 8px;
  left: 17px;
}

.search {
  width: 100%;
  :global(.next-input input) {
    padding-left: 33px;
  }
  :global(.next-input-group .next-input:hover) {
    position: inherit !important;
  }
}

.search:global(.next-search-btn) {
  font-size: 12px !important;
}

.contentBox {
  /* margin-top: 22px; */
  height: 510px;
  /* max-height: 530px; */
  display: flex;
  flex-direction: row;
}

.categoryBox {
  width: 160px;
  min-width: 160px;
  overflow-x: hidden;
  overflow-y: auto;
}

.categoryBox .categoryList {
  height: 100%;
  padding-top: 0;
  border: none;
  border-right: 1px solid #dedede;
}

.categoryBox .categoryList:global(.next-selected) {
  border-right: 2px solid #0070cc;
}

.categoryBox .categoryList li.selectedCate {
  border-right: 2px solid #0070cc;
}

.clearSearch {
  margin-left: 10px;
  color: #0070cc;
  cursor: pointer;
}

.listBox {
  position: relative;
  overflow: hidden;
  flex-grow: 1;
  padding-left: 4px;

  .functionList {
    list-style: none;
    overflow: hidden;
    min-height: 224px;
  }

  .functionList li {
    width: 257px;
    height: 40px;
    font-size: 12px;
    line-height: 40px;
    text-align: center;
    border: 1px solid #dedede;
    margin: 0 8px 16px 0;
    cursor: pointer;
    float: left;
  }

  :global(.next-pagination) {
    float: none;
    text-align: right;
    margin-bottom: 8px;
    margin-top: -8px;
    margin-right: 17px;
  }
}

.listBox .functionList li.selectedFunc {
  color: #0070cc;
  border: 1px solid #0070cc;
}

.loading {
  width: 100%;
}

.descriptionBox {
  margin-right: 16px;
  padding: 5px;
  height: 220px;
  overflow-y: auto;
  font-size: 12px;
  color: #000;
  letter-spacing: 0;
  line-height: 18px;
  border: 1px solid #dedede;
}

.btnRow {
  text-align: right;
}

.btn {
  margin-left: 8px;
}
`],sourceRoot:""}]),o.locals={container:"index__container__-R1ZY",searchBox:"index__searchBox__4itYE",icon:"index__icon__Yrchm",search:"index__search__PJKPg",contentBox:"index__contentBox__lWDuC",categoryBox:"index__categoryBox__PsQka",categoryList:"index__categoryList__8lpnx",selectedCate:"index__selectedCate__fNyLs",clearSearch:"index__clearSearch__GLS+S",listBox:"index__listBox__2s+St",functionList:"index__functionList__8+lTI",selectedFunc:"index__selectedFunc__SnSVV",loading:"index__loading__IvmoD",descriptionBox:"index__descriptionBox__KVOZB",btnRow:"index__btnRow__f0+Nv",btn:"index__btn__5PaRI"};const A=o},87223:(z,f,e)=>{"use strict";e.d(f,{Z:()=>A});var r=e(60994),B=e.n(r),s=e(93476),g=e.n(s),o=g()(B());o.push([z.id,`.index__boxContainer__yI63h, .index__boxContainerReadOnly__rLzIJ {
  display: flex;
  background-color: #f7f7f7;
  margin-right: 70px;
}

.index__boxContainerReadOnly__rLzIJ {
  background-color: transparent;
}

.index__boxContainerReadOnly__rLzIJ .index__box__9KijU {
  padding: 0;
}

.index__boxContainer__yI63h {
  margin-top: 20px;
}

.index__box__9KijU {
  padding: 24px 23px;
  display: flex;
  flex-grow: 1;
  flex-direction: row;
  flex-wrap: wrap;
}

.index__deleteFlowBox__AXAIQ {
  width: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  flex-shrink: 0;
}`,"",{version:3,sources:["webpack://./pages/Chaos/common/MiniFlow/index.css"],names:[],mappings:"AAAA;EACE,aAAa;EACb,yBAAyB;EACzB,kBAAkB;AACpB;;AAEA;EACE,6BAA6B;AAC/B;;AAEA;EACE,UAAU;AACZ;;AAEA;EACE,gBAAgB;AAClB;;AAEA;EACE,kBAAkB;EAClB,aAAa;EACb,YAAY;EACZ,mBAAmB;EACnB,eAAe;AACjB;;AAEA;EACE,WAAW;EACX,aAAa;EACb,uBAAuB;EACvB,mBAAmB;EACnB,eAAe;EACf,cAAc;AAChB",sourcesContent:[`.boxContainer, .boxContainerReadOnly {
  display: flex;
  background-color: #f7f7f7;
  margin-right: 70px;
}

.boxContainerReadOnly {
  background-color: transparent;
}

.boxContainerReadOnly .box {
  padding: 0;
}

.boxContainer {
  margin-top: 20px;
}

.box {
  padding: 24px 23px;
  display: flex;
  flex-grow: 1;
  flex-direction: row;
  flex-wrap: wrap;
}

.deleteFlowBox {
  width: 32px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  flex-shrink: 0;
}`],sourceRoot:""}]),o.locals={boxContainer:"index__boxContainer__yI63h",boxContainerReadOnly:"index__boxContainerReadOnly__rLzIJ",box:"index__box__9KijU",deleteFlowBox:"index__deleteFlowBox__AXAIQ"};const A=o},7137:(z,f,e)=>{"use strict";e.d(f,{Z:()=>A});var r=e(60994),B=e.n(r),s=e(93476),g=e.n(s),o=g()(B());o.push([z.id,`.index__normalNodeContainer__Vy6PM,
.index__globalNodeContainer__c2iqs {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  cursor: pointer;
}

.index__globalNodeContainer__c2iqs {
  margin-right: 29px;
  margin-bottom: 8px;
}

.index__nodeBox__L575N {
  transition: all .3s;
  position: relative;
  padding: 5px 0;
  width: 244px;
  height: 64px;
  border: 1px solid #dedede;
  border-radius: 3px;
  text-align: center;
  background: #fff;
  position: relative;
  overflow: hidden;
}

.index__nodeBox__L575N .index__topLayer__YoyPy, .index__bottomLayer__EGcfl {
  height: 50%;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.index__nodeBox__L575N:hover,
.index__selectedNode__NNk6g {
  border: 1px solid #0070CC;
  box-shadow: 0 0 4px 0 rgba(0,112,204,0.75);
}

.index__errorNode__SEDtu,
.index__errorNode__SEDtu:hover {
  border: 1px solid #D93026;
  box-shadow: 0 0 4px 0 rgba(217,48,38,0.75);
}

.index__nodeBox__L575N .next-icon-ashbin {
  display: none;
}
.index__nodeBox__L575N:hover .next-icon-ashbin {
  display: block;
}

.index__selectedTriangle__Oqr\\+U {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 10px 0 0 10px;
  border-color: transparent transparent transparent #0070cc;
}

.index__Icon__xQT\\+I {
  margin-right: 8px;
}

.index__topLeftBox__9moQS,
.index__topRightBox__4A70p,
.index__bottomLeftBox__nTv\\+6,
.index__bottomRightBox__RUa5D {
  display: flex;
  align-items: center;
  margin: 0;
  height: 100%;
}

.index__topLeftBox__9moQS .index__stageIcon__tpXvL {
  margin-left: 12px;
  margin-right: 4px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
}

.index__topLeftBox__9moQS .index__nodeName__wTYa9 {
  display: block;
  max-width: 140px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.index__bottomLeftBox__nTv\\+6 .index__parameterInfo__96vV4 {
  width: 100px;
  overflow: hidden;
  margin-left: 12px;
  color: #888;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.index__firstBox__pQ99F {
  width: 16px;
  min-width: 16px;
}

.index__headBox__qFHO8, .index__tailBox__9yi1O {
  position: relative;
  height: 64px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.index__iconHead__vySzz, .index__iconTail__LXrO0, .index__switchArrowIcon__FaGZ0 {
  position: absolute;
  top: 25px;
  width: 14px;
  height: 14px;
  cursor: pointer;
  color: #0070cc;
  font-weight: bold;
}

.index__iconHead__vySzz {
  left: -6px;
}

.index__iconTail__LXrO0 {
  left: 15px;
}

.index__tailBox__9yi1O,
.index__tailBoxReadOnly__0sFpl {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.index__tailBox__9yi1O,
.index__tailBoxReadOnly__0sFpl .next-icon-caret-right {
  color: #ebebeb;
  margin-right: -5px;
}

.index__tailBox__9yi1O {
  width: 48px;
}

.index__tailBoxReadOnly__0sFpl {
  width: 29px;
}

.index__circle__vnKfM {
  margin-left: -3px;
  width: 5px;
  height: 5px;
  border-radius: 5px;
  background-color: #fff;
  border: 1px solid #dedede;
}

.index__lastCircle__2Ijzo {
  position: absolute;
  left: 124px;
  bottom: -2px;
}

.index__arrowLineHead__IKRb5,
.index__arrowLineTail__Qew1w {
  height: 2px;
  background-color: #ebebeb;
}

.index__arrowLineHead__IKRb5 {
  width: 16px;
}

.index__arrowLineTail__Qew1w {
  flex-grow: 1;
  margin-right: -5px;
}

.index__switchArrowContainer__g-hjn {
  position: relative;
  height: 38px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.index__switchArrowDownContainer__YPtrF {
  position: relative;
  width: 2px;
  height: 100%;
}

.index__switchArrowDown__9Y1gq, .index__switchArrowUp__S4BW8 {
  position: absolute;
  left: 0;
  width: 2px;
  height: 20px;
  background-color: #ebebeb;
}

.index__switchArrowDown__9Y1gq {
  bottom: 0;
}

.index__switchArrowUp__S4BW8 {
  top: 0;
}

.index__switchArrowDownIcon__zWpXt {
  position: absolute;
  left: -7px;
  bottom: -3px;
  color: #ebebeb;
}

.index__switchArrowDownIcon__zWpXt:before {
  height: 16px !important;
}

.index__switchArrowLine__wWH4C {
  height: 2px;
  background-color: #ebebeb;
}

.index__switchArrowUpContainer__6UWN4 {
  position: relative;
  width: 1px;
  height: 100%;
}

.index__switchArrowIcon__FaGZ0 {
  top: 12px;
  left: -7px;
}

.index__groupOrderBox__3NNud {
  margin-right: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  width: 45px;
  height: 20px;
  border-radius: 10px;
  background-color: #ebebeb;
  color: #c1c1c1;
}

.index__action__MfFWc {
  font-size: 12px;
  color: #0070CC;
  cursor: pointer;
}

.index__userCheck__yrAUG {
  font-size: 12px;
  color: #0070CC;
  cursor: pointer;
  z-index: 99;
}

.index__userAction__dOpzq {
  margin-left: 5px;
  margin-right: 10px;
}

.index__userAction__dOpzq .index__contiueTryIcon__19jn4 {
    margin-right: 10px;
  }

/* \u6267\u884C\u8282\u70B9\u6837\u5F0F */
.index__activityRunning__zKrBm {
  border: 1px solid #DEDEDE;
}

.index__activityRejected__GBzVm {
  border: 1px solid #86D8F8;
}

.index__activitySuccess__p4-Rg {
  border: 1px solid #1E8E3E;
}

.index__activityFailed__RckCx {
  border: 1px solid #D93026;
}`,"",{version:3,sources:["webpack://./pages/Chaos/common/Node/index.css"],names:[],mappings:"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",sourcesContent:[`.normalNodeContainer,
.globalNodeContainer {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  cursor: pointer;
}

.globalNodeContainer {
  margin-right: 29px;
  margin-bottom: 8px;
}

.nodeBox {
  transition: all .3s;
  position: relative;
  padding: 5px 0;
  width: 244px;
  height: 64px;
  border: 1px solid #dedede;
  border-radius: 3px;
  text-align: center;
  background: #fff;
  position: relative;
  overflow: hidden;
}

.nodeBox .topLayer, .bottomLayer {
  height: 50%;
  overflow: hidden;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nodeBox:hover,
.selectedNode {
  border: 1px solid #0070CC;
  box-shadow: 0 0 4px 0 rgba(0,112,204,0.75);
}

.errorNode,
.errorNode:hover {
  border: 1px solid #D93026;
  box-shadow: 0 0 4px 0 rgba(217,48,38,0.75);
}

.nodeBox :global(.next-icon-ashbin) {
  display: none;
}
.nodeBox:hover :global(.next-icon-ashbin) {
  display: block;
}

.selectedTriangle {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 10px 0 0 10px;
  border-color: transparent transparent transparent #0070cc;
}

.Icon {
  margin-right: 8px;
}

.topLeftBox,
.topRightBox,
.bottomLeftBox,
.bottomRightBox {
  display: flex;
  align-items: center;
  margin: 0;
  height: 100%;
}

.topLeftBox .stageIcon {
  margin-left: 12px;
  margin-right: 4px;
  width: 10px;
  height: 10px;
  border-radius: 5px;
}

.topLeftBox .nodeName {
  display: block;
  max-width: 140px;
  color: #333;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.bottomLeftBox .parameterInfo {
  width: 100px;
  overflow: hidden;
  margin-left: 12px;
  color: #888;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.firstBox {
  width: 16px;
  min-width: 16px;
}

.headBox, .tailBox {
  position: relative;
  height: 64px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.iconHead, .iconTail, .switchArrowIcon {
  position: absolute;
  top: 25px;
  width: 14px;
  height: 14px;
  cursor: pointer;
  color: #0070cc;
  font-weight: bold;
}

.iconHead {
  left: -6px;
}

.iconTail {
  left: 15px;
}

.tailBox,
.tailBoxReadOnly {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.tailBox,
.tailBoxReadOnly :global(.next-icon-caret-right) {
  color: #ebebeb;
  margin-right: -5px;
}

.tailBox {
  width: 48px;
}

.tailBoxReadOnly {
  width: 29px;
}

.circle {
  margin-left: -3px;
  width: 5px;
  height: 5px;
  border-radius: 5px;
  background-color: #fff;
  border: 1px solid #dedede;
}

.lastCircle {
  position: absolute;
  left: 124px;
  bottom: -2px;
}

.arrowLineHead,
.arrowLineTail {
  height: 2px;
  background-color: #ebebeb;
}

.arrowLineHead {
  width: 16px;
}

.arrowLineTail {
  flex-grow: 1;
  margin-right: -5px;
}

.switchArrowContainer {
  position: relative;
  height: 38px;
  display: flex;
  flex-direction: row;
  align-items: center;
}

.switchArrowDownContainer {
  position: relative;
  width: 2px;
  height: 100%;
}

.switchArrowDown, .switchArrowUp {
  position: absolute;
  left: 0;
  width: 2px;
  height: 20px;
  background-color: #ebebeb;
}

.switchArrowDown {
  bottom: 0;
}

.switchArrowUp {
  top: 0;
}

.switchArrowDownIcon {
  position: absolute;
  left: -7px;
  bottom: -3px;
  color: #ebebeb;
}

.switchArrowDownIcon:before {
  height: 16px !important;
}

.switchArrowLine {
  height: 2px;
  background-color: #ebebeb;
}

.switchArrowUpContainer {
  position: relative;
  width: 1px;
  height: 100%;
}

.switchArrowIcon {
  top: 12px;
  left: -7px;
}

.groupOrderBox {
  margin-right: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  width: 45px;
  height: 20px;
  border-radius: 10px;
  background-color: #ebebeb;
  color: #c1c1c1;
}

.action {
  font-size: 12px;
  color: #0070CC;
  cursor: pointer;
}

.userCheck {
  font-size: 12px;
  color: #0070CC;
  cursor: pointer;
  z-index: 99;
}

.userAction {
  margin-left: 5px;
  margin-right: 10px;

  .contiueTryIcon {
    margin-right: 10px;
  }
}

/* \u6267\u884C\u8282\u70B9\u6837\u5F0F */
.activityRunning {
  border: 1px solid #DEDEDE;
}

.activityRejected {
  border: 1px solid #86D8F8;
}

.activitySuccess {
  border: 1px solid #1E8E3E;
}

.activityFailed {
  border: 1px solid #D93026;
}`],sourceRoot:""}]),o.locals={normalNodeContainer:"index__normalNodeContainer__Vy6PM",globalNodeContainer:"index__globalNodeContainer__c2iqs",nodeBox:"index__nodeBox__L575N",topLayer:"index__topLayer__YoyPy",bottomLayer:"index__bottomLayer__EGcfl",selectedNode:"index__selectedNode__NNk6g",errorNode:"index__errorNode__SEDtu",selectedTriangle:"index__selectedTriangle__Oqr+U",Icon:"index__Icon__xQT+I",topLeftBox:"index__topLeftBox__9moQS",topRightBox:"index__topRightBox__4A70p",bottomLeftBox:"index__bottomLeftBox__nTv+6",bottomRightBox:"index__bottomRightBox__RUa5D",stageIcon:"index__stageIcon__tpXvL",nodeName:"index__nodeName__wTYa9",parameterInfo:"index__parameterInfo__96vV4",firstBox:"index__firstBox__pQ99F",headBox:"index__headBox__qFHO8",tailBox:"index__tailBox__9yi1O",iconHead:"index__iconHead__vySzz",iconTail:"index__iconTail__LXrO0",switchArrowIcon:"index__switchArrowIcon__FaGZ0",tailBoxReadOnly:"index__tailBoxReadOnly__0sFpl",circle:"index__circle__vnKfM",lastCircle:"index__lastCircle__2Ijzo",arrowLineHead:"index__arrowLineHead__IKRb5",arrowLineTail:"index__arrowLineTail__Qew1w",switchArrowContainer:"index__switchArrowContainer__g-hjn",switchArrowDownContainer:"index__switchArrowDownContainer__YPtrF",switchArrowDown:"index__switchArrowDown__9Y1gq",switchArrowUp:"index__switchArrowUp__S4BW8",switchArrowDownIcon:"index__switchArrowDownIcon__zWpXt",switchArrowLine:"index__switchArrowLine__wWH4C",switchArrowUpContainer:"index__switchArrowUpContainer__6UWN4",groupOrderBox:"index__groupOrderBox__3NNud",action:"index__action__MfFWc",userCheck:"index__userCheck__yrAUG",userAction:"index__userAction__dOpzq",contiueTryIcon:"index__contiueTryIcon__19jn4",activityRunning:"index__activityRunning__zKrBm",activityRejected:"index__activityRejected__GBzVm",activitySuccess:"index__activitySuccess__p4-Rg",activityFailed:"index__activityFailed__RckCx"};const A=o},54581:(z,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var r=e(1892),B=e.n(r),s=e(94539),g={};g.insert="head",g.singleton=!1;var o=B()(s.Z,g);const A=s.Z.locals||{}},39725:(z,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var r=e(1892),B=e.n(r),s=e(87223),g={};g.insert="head",g.singleton=!1;var o=B()(s.Z,g);const A=s.Z.locals||{}},1918:(z,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>A});var r=e(1892),B=e.n(r),s=e(7137),g={};g.insert="head",g.singleton=!1;var o=B()(s.Z,g);const A=s.Z.locals||{}}}]);

//# sourceMappingURL=313.bundle.js.map