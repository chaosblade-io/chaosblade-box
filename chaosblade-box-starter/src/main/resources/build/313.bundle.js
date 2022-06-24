(self.webpackChunk=self.webpackChunk||[]).push([[313],{17047:function(V,f,e){var A,x,u,B=e(67394);(function(o,d){if(!0)!(x=[f,e(14798)],A=d,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,d){"use strict";var M=e(67971);B(o,"__esModule",{value:!0}),o.TASK=o.RECOVER=o.OBSERVER=o.NodeRunResult=o.NORMAL=o.ExperimentConstants=void 0,d=M(d);var y={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:d.default.t("Abnormal"),FAILED:d.default.t("Not as expected"),STOPPED:d.default.t("Interrupt"),SUCCESS:d.default.t("Success"),EXCEPTION:d.default.t("Abnormal"),TOTAL:d.default.t("Number of drills")};o.ExperimentConstants=y;var E={REJECTED:"REJECTED",SUCCESS:"SUCCESS",FAILED:["FAILED","SOPPED_FAILED","ERROR","STOPPED"]};o.NodeRunResult=E;var C=-1;o.NORMAL=C;var Ae=0;o.OBSERVER=Ae;var T=1;o.RECOVER=T;var c=2;o.TASK=c})},96042:function(V,f,e){var A,x,u,B=e(24596),o=e(67394),d=e(93168),M=e(23587),y=e(41281),E=e(50093),C=e(59396),Ae=e(75453);(function(T,c){if(!0)!(x=[f,e(73220),e(12955),e(93484),e(94188),e(17225),e(72153),e(77809),e(81853),e(85645),e(27378),e(66697),e(98784),e(14798),e(68055),e(54581),e(73262),e(14870)],A=c,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var n})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(T,c,n,p,F,ee,R,i,s,ie,l,ne,Z,te,me,k,t,H){"use strict";var _=e(67971);o(T,"__esModule",{value:!0}),T.default=void 0,c=_(c),n=_(n),p=_(p),F=_(F),ee=_(ee),R=_(R),i=_(i),s=_(s),ie=_(ie),l=_e(l),ne=_(ne),Z=_(Z),te=_(te),me=_(me),k=_(k);function $(a){if(typeof d!="function")return null;var P=new d,m=new d;return($=function(D){return D?m:P})(a)}function _e(a,P){if(!P&&a&&a.__esModule)return a;if(a===null||B(a)!=="object"&&typeof a!="function")return{default:a};var m=$(P);if(m&&m.has(a))return m.get(a);var ae={},D=o&&M;for(var G in a)if(G!=="default"&&Object.prototype.hasOwnProperty.call(a,G)){var Ee=D?M(a,G):null;Ee&&(Ee.get||Ee.set)?o(ae,G,Ee):ae[G]=a[G]}return ae.default=a,m&&m.set(a,ae),ae}function K(a,P){var m=typeof y!="undefined"&&a[E]||a["@@iterator"];if(!m){if(C(a)||(m=se(a))||P&&a&&typeof a.length=="number"){m&&(a=m);var ae=0,D=function(){};return{s:D,n:function(){return ae>=a.length?{done:!0}:{done:!1,value:a[ae++]}},e:function(w){throw w},f:D}}throw new TypeError(`Invalid attempt to iterate non-iterable instance.
In order to be iterable, non-array objects must have a [Symbol.iterator]() method.`)}var G=!0,Ee=!1,Re;return{s:function(){m=m.call(a)},n:function(){var w=m.next();return G=w.done,w},e:function(w){Ee=!0,Re=w},f:function(){try{!G&&m.return!=null&&m.return()}finally{if(Ee)throw Re}}}}function se(a,P){if(!a)return;if(typeof a=="string")return we(a,P);var m=Object.prototype.toString.call(a).slice(8,-1);if(m==="Object"&&a.constructor&&(m=a.constructor.name),m==="Map"||m==="Set")return Ae(a);if(m==="Arguments"||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(m))return we(a,P)}function we(a,P){(P==null||P>a.length)&&(P=a.length);for(var m=0,ae=new Array(P);m<P;m++)ae[m]=a[m];return ae}var Te=ie.default.SubMenu,Ie=ie.default.Item;function We(a){var P=(0,H.useDispatch)(),m=(0,l.useState)([]),ae=(0,s.default)(m,2),D=ae[0],G=ae[1],Ee=(0,l.useState)(""),Re=(0,s.default)(Ee,2),O=Re[0],w=Re[1],v=(0,l.useState)(1),Ne=(0,s.default)(v,2),xe=Ne[0],ve=Ne[1],De=(0,l.useState)(1),ge=(0,s.default)(De,2),Me=ge[0],Pe=ge[1],Oe=(0,l.useState)(1),Se=(0,s.default)(Oe,2),oe=Se[0],Q=Se[1],Fe=(0,l.useState)(null),Y=(0,s.default)(Fe,2),r=Y[0],z=Y[1],ue=(0,l.useState)([]),re=(0,s.default)(ue,2),I=re[0],J=re[1],ke=(0,l.useState)(""),Be=(0,s.default)(ke,2),fe=Be[0],ce=Be[1],Ke=(0,l.useState)([]),L=(0,s.default)(Ke,2),N=L[0],j=L[1],de=(0,l.useState)(!1),he=(0,s.default)(de,2),pe=he[0],Le=he[1],Ze=(0,l.useState)(!1),Xe=(0,s.default)(Ze,2),Ve=Xe[0],He=Xe[1];(0,l.useEffect)(function(){a.visible||(G([]),w(""),ve(1),z(null),J([]),ce(""),j([]),Le(!1))},[a.visible]),(0,l.useEffect)(function(){if(ve(1),G([]),w(""),J([]),z(null),j([]),pe){var g=a.phase,S=a.scopeType,W=a.k8sResourceType;(0,i.default)(regeneratorRuntime.mark(function X(){return regeneratorRuntime.wrap(function(q){for(;;)switch(q.prev=q.next){case 0:return q.next=2,P.experimentScene.searchFunctions({key:fe,phase:g,page:1,scopeType:S,k8sResourceType:W},function(h){var U=h.data,b=U===void 0?[]:U,Ce=h.pageSize,Ue=Ce===void 0?1:Ce,Ye=h.total,ye=Ye===void 0?1:Ye;Q(ye),Pe(Ue),J(b)});case 2:case"end":return q.stop()}},X)}))()}else ce("")},[pe,Ve]),(0,l.useEffect)(function(){if(!a.visible)return;if(!pe){var g=a.phase,S=a.scopeType,W=a.nodeType,X=a.cloudServiceType,le=a.osType;W===t.NODE_TYPE.OBSERVER?(0,i.default)(regeneratorRuntime.mark(function q(){return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,P.experimentScene.getGlobalCategories(function(b){Z.default.isEqual(D,b)||G(b)});case 2:case"end":return U.stop()}},q)}))():W===t.NODE_TYPE.RECOVER?(0,i.default)(regeneratorRuntime.mark(function q(){return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,P.experimentScene.getGuardCategories(function(b){Z.default.isEqual(D,b)||G(b)});case 2:case"end":return U.stop()}},q)}))():(0,i.default)(regeneratorRuntime.mark(function q(){var h;return regeneratorRuntime.wrap(function(b){for(;;)switch(b.prev=b.next){case 0:return b.next=2,P.experimentScene.getCategories({phase:g,scopeType:S,filterNoChild:!0,cloudServiceType:X,osType:le});case 2:h=b.sent,Z.default.isEqual(D,h)||G(h);case 4:case"end":return b.stop()}},q)}))()}},[a.visible,a.phase,a.scopeType,a.nodeType,pe,a.osType]),(0,l.useEffect)(function(){if(D.length>0){for(var g=D[0];g.children&&g.children.length>0;)g=g.children[0];w(g.categoryId)}},[D]),(0,l.useEffect)(function(){ve(1);var g=[];S(D,g),g.length>0?j([].concat(g)):D.length>0&&j([D[0].categoryId]);function S(W,X){var le=K(W),q;try{for(le.s();!(q=le.n()).done;){var h=q.value;if(h.children&&h.children.length>0){if(X.push(h.categoryId),S(h.children,X))return!0;X.pop()}else{if(h.categoryId!==O)continue;return!0}}}catch(U){le.e(U)}finally{le.f()}return!1}},[O]),(0,l.useEffect)(function(){if(!a.visible)return;var g=a.phase,S=a.scopeType,W=a.k8sResourceType,X=a.osType;pe?(0,i.default)(regeneratorRuntime.mark(function le(){return regeneratorRuntime.wrap(function(h){for(;;)switch(h.prev=h.next){case 0:return h.next=2,P.experimentScene.searchFunctions({key:fe,phase:g,page:xe,scopeType:S,k8sResourceType:W,osType:X},function(U){var b=U.data,Ce=b===void 0?[]:b,Ue=U.total,Ye=Ue===void 0?1:Ue,ye=U.pageSize,je=ye===void 0?1:ye;Q(Ye),Pe(je),Z.default.isEqual(I,Ce)||J(Ce)});case 2:case"end":return h.stop()}},le)}))():O&&xe&&(0,i.default)(regeneratorRuntime.mark(function le(){return regeneratorRuntime.wrap(function(h){for(;;)switch(h.prev=h.next){case 0:return h.next=2,P.experimentScene.getFunctionsByCategoryId({page:xe,categoryId:O,phase:g,scopeType:S,k8sResourceType:W,size:12,osType:X},function(U){if(U){var b=U.data,Ce=U.total,Ue=U.pageSize;Q(Ce),Pe(Ue),Z.default.isEqual(I,b)||J(b)}});case 2:case"end":return h.stop()}},le)}))()},[a.phase,a.scopeType,O,xe,a.k8sResourceType,a.osType]),(0,l.useEffect)(function(){I.length>0?z(I[0]):z(null)},[I]);var ze=function(){a.onClose()},$e=function(S){return ce(S)},Ge=function(){Le(!0),He(!Ve)},Qe=function(){Le(!pe)},Je=function(S){S!==O&&w(S)},qe=function(S){var W=I.filter(function(X){return X.functionId===S});W.length>0&&z(W[0])},en=function(S){ve(S)},nn=function(S){return l.default.createElement(ie.default,{className:k.default.categoryList,selectMode:"single",defaultOpenKeys:N,selectedKeys:O,hasSelectedIcon:!1,onItemClick:Je,inlineIndent:10},tn(S))},tn=function g(S){var W=[],X=K(S),le;try{for(X.s();!(le=X.n()).done;){var q=le.value,h=q.categoryId,U=q.name,b=q.children;b&&b.length>0?W.push(l.default.createElement(Te,{key:h,label:U},g(b))):W.push(l.default.createElement(Ie,{key:h},U))}}catch(Ce){X.e(Ce)}finally{X.f()}return W},an=function(){var g=(0,i.default)(regeneratorRuntime.mark(function S(){var W,X,le;return regeneratorRuntime.wrap(function(h){for(;;)switch(h.prev=h.next){case 0:if(W=a.phase,!(W&&r&&!r.arguments||W&&!r)){h.next=6;break}return h.next=4,new c.default(function(U,b){var Ce=Z.default.get(r,"functionId","");(0,i.default)(regeneratorRuntime.mark(function Ue(){return regeneratorRuntime.wrap(function(ye){for(;;)switch(ye.prev=ye.next){case 0:return ye.next=2,P.experimentScene.getFunctionParameters({functionId:Ce},function(je,ln){je?b(je):U(ln)});case 2:case"end":return ye.stop()}},Ue)}))()});case 4:X=h.sent,Z.default.set(r,"arguments",X);case 6:le=a.onSelect,le(Z.default.cloneDeep(r)),ze();case 9:case"end":return h.stop()}},S)}));return function(){return g.apply(this,arguments)}}();return l.default.createElement(n.default,{title:a.title,visible:a.visible,footer:l.default.createElement("div",{className:k.default.btnRow},l.default.createElement(R.default,{className:k.default.btn,type:"primary",disabled:I.length===0,onClick:an,"data-autolog":"text=".concat(te.default.t("Confirm selection of applet").toString())},l.default.createElement(ne.default,null,"Confirm")),l.default.createElement(R.default,{className:k.default.btn,type:"normal",onClick:ze,"data-autolog":"".concat(te.default.t("Deselect applet"))},l.default.createElement(ne.default,null,"cancel"))),onCancel:ze,onClose:ze,style:{minWidth:968},locale:(0,me.default)().Dialog},l.default.createElement("div",{className:k.default.container},a.searchable?l.default.createElement("div",{className:k.default.searchBox},l.default.createElement(ee.default,{type:"search",size:"xs",className:k.default.icon}),l.default.createElement(F.default,{className:k.default.search,value:fe,onChange:$e,onSearch:Ge,placeholder:te.default.t("Search for fault titles").toString(),searchText:te.default.t("Search").toString(),hasIcon:!1})):null,l.default.createElement("div",{className:k.default.contentBox},l.default.createElement("div",{className:k.default.categoryBox},!pe&&N.length>0&&nn(D),pe&&l.default.createElement("p",null,l.default.createElement("span",null,l.default.createElement(ne.default,null,"Search results"),":"),l.default.createElement("span",{className:k.default.clearSearch,onClick:Qe},l.default.createElement(ne.default,null,"Empty")))),l.default.createElement("div",{className:k.default.listBox},l.default.createElement("ul",{className:k.default.functionList},Z.default.isEmpty(I)?l.default.createElement("div",null,l.default.createElement(ne.default,null,"There is no scene in the current category")):I.map(function(g){var S=g.name,W=g.functionId,X=!1;return r&&(r.functionId===W&&(X=!0)),l.default.createElement("li",{className:X?k.default.selectedFunc:void 0,key:W,onClick:function(){return qe(W)},"data-autolog":"text=".concat(te.default.t("Click the applet").toString())},S)})),l.default.createElement(p.default,{shape:"arrow-only",current:xe,total:oe,pageSize:Me,hideOnlyOnePage:!0,locale:(0,me.default)().Pagination,onChange:function(S){return en(S)}}),r?l.default.createElement("div",{className:k.default.descriptionBox},l.default.createElement("p",null,r.description)):l.default.createElement("div",{style:{minHeight:220}})))))}var be=We;T.default=be})},63835:function(V,f,e){var A,x,u,B=e(67394);(function(o,d){if(!0)!(x=[f,e(93080),e(59607),e(32186),e(27378)],A=d,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,d,M,y,E){"use strict";var C=e(67971);B(o,"__esModule",{value:!0}),o.default=void 0,d=C(d),M=C(M),y=C(y),E=C(E);var Ae=["type","children"],T=new y.default,c=function(ee){var R=ee.scriptUrl,i=ee.extraCommonProps,s=i===void 0?{}:i;if(typeof document!="undefined"&&typeof window!="undefined"&&typeof document.createElement=="function"&&typeof R=="string"&&R.length&&!T.has(R)){var ie=document.createElement("script");ie.setAttribute("src",R),ie.setAttribute("data-namespace",R),T.add(R),document.body.appendChild(ie)}var l=function(Z){var te=Z.type,me=Z.children,k=(0,M.default)(Z,Ae),t=null;return Z.type&&(t=E.default.createElement("use",{xlinkHref:"#".concat(te)})),me&&(t=me),E.default.createElement("svg",(0,d.default)({},k,s),t)};return l.displayName="Iconfont",l},n=c({scriptUrl:"//at.alicdn.com/t/font_976326_mtiq05ajtqs.js"}),p=n;o.default=p})},76313:function(V,f,e){var A,x,u,B=e(24596),o=e(67394),d=e(93168),M=e(23587);(function(y,E){if(!0)!(x=[f,e(17225),e(81853),e(96042),e(9577),e(27378),e(89724),e(98784),e(14798),e(39725)],A=E,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(y,E,C,Ae,T,c,n,p,F,ee){"use strict";var R=e(67971);o(y,"__esModule",{value:!0}),y.default=void 0,E=R(E),C=R(C),Ae=R(Ae),T=R(T),c=s(c),n=R(n),p=R(p),F=R(F),ee=R(ee);function i(t){if(typeof d!="function")return null;var H=new d,_=new d;return(i=function(_e){return _e?_:H})(t)}function s(t,H){if(!H&&t&&t.__esModule)return t;if(t===null||B(t)!=="object"&&typeof t!="function")return{default:t};var _=i(H);if(_&&_.has(t))return _.get(t);var $={},_e=o&&M;for(var K in t)if(K!=="default"&&Object.prototype.hasOwnProperty.call(t,K)){var se=_e?M(t,K):null;se&&(se.get||se.set)?o($,K,se):$[K]=t[K]}return $.default=t,_&&_.set(t,$),$}var ie=16,l=16,ne=244,Z=0,te=0;function me(t){var H=(0,c.useRef)(null),_;t.editable?te=48:te=29,t.deletable?Z=32:Z=0;var $=(0,c.useState)(0),_e=(0,C.default)($,2),K=_e[0],se=_e[1],we=(0,c.useState)(NaN),Te=(0,C.default)(we,2),Ie=Te[0],We=Te[1],be=(0,c.useState)(),a=(0,C.default)(be,2),P=a[0],m=a[1],ae=(0,c.useState)(),D=(0,C.default)(ae,2),G=D[0],Ee=D[1],Re=(0,c.useState)(!1),O=(0,C.default)(Re,2),w=O[0],v=O[1];(0,c.useEffect)(function(){Ne(),xe()},[]),(0,c.useEffect)(function(){return function(){_&&clearTimeout(_)}},[]);function Ne(){var Y=t.editable,r=H.current&&H.current.scrollWidth-(Y?ie*2:0);r!==K&&se(r)}function xe(){var Y=1e3;_&&clearTimeout(_),_=setTimeout(function(){Ne(),xe()},Y)}var ve=function(r,z){var ue=t.onNodeAdding;ue&&ue();var re;r?r.phase===1<<1?z?z.phase===1<<3?re=1<<2|1<<3:z.phase&&(re=z.phase):re=1<<2|1<<3:r.phase&&(re=r.phase):re=1<<0,m(r),Ee(z),We(re),v(!0)};function De(){v(!1)}function ge(Y){var r;switch(Ie){case 1<<0:r="prepare";break;case 1<<1:r="attack";break;case 1<<2:r="check";break;case 1<<3:r="recover";break;default:r=void 0;break}if(!r){var z=p.default.get(Y,"phaseFlag");z&&((z&8)===8?r="recover":r="check")}Y.stage=r,P?P.insertAfter(Y):G&&G.insertBefore(Y);var ue=t.onNodeAdd;ue&&ue(Y)}function Me(Y,r){var z=t.editable,ue=Y-Z,re=z?l+ne+te:ne+te,I=0;if(ue>=re){I=1;for(var J=ue-re;J>=te+ne;)++I,J-=te+ne;J>ne&&r>I+1&&++I}return I}function Pe(Y){var r=t.editable,z=t.selectedNode,ue=t.isExpertise,re=t.running,I=Me(K,Y.length),J=ne/2+(r?l:0),ke=(ne+te)*(I-1),Be=[];return K>0&&I>0&&p.default.forEach(Y,function(fe,ce){var Ke=fe.id,L=fe.activityId,N=z&&fe.id===z.id,j=ce===0,de=ce!==0&&(ce+1)%I===1,he=ce!==Y.length-1&&(ce+1)%I===0,pe=ce===Y.length-1;Be.push(c.default.createElement(T.default,{key:Ke||L,data:fe,editable:r,isExpertise:ue,selected:N,isHead:j,isLineFirst:de,isLineLast:he,isLast:pe,onClick:t.onNodeClick,onNodeAddClick:ve,onNodeDeleteClick:t.onNodeDelete,onTryAgainClick:t.onTryAgain,running:re,onCheck:t.onCheck,permission:t.permission})),he&&Be.push(c.default.createElement(n.default,{key:ce,data:fe,editable:r,lineWidth:ke,padding:J,onNodeAddClick:ve}))}),Be}var Oe=t.editable,Se=t.nodes,oe=t.scopeType,Q=t.deletable,Fe=Oe?ee.default.boxContainer:ee.default.boxContainerReadOnly;return p.default.isEmpty(Se)?null:c.default.createElement("div",{className:Fe},c.default.createElement("div",{ref:H,className:ee.default.box},Pe(Se)),Q&&c.default.createElement("div",{className:ee.default.deleteFlowBox,onClick:t.onDelete,"data-autolog":"text=".concat(F.default.t("Delete walkthrough group"))},c.default.createElement(E.default,{type:"ashbin",size:"small",style:{color:"#888"},"data-autolog":"text=".concat(F.default.t("Delete walkthrough group"))})),c.default.createElement(Ae.default,{title:F.default.t("Choose a walkthrough node").toString(),searchable:!0,visible:w,phase:Ie,scopeType:oe,onClose:De,onSelect:ge}))}var k=me;y.default=k})},89724:function(V,f,e){var A,x,u,B=e(67394);(function(o,d){if(!0)!(x=[f,e(17225),e(63835),e(27378),e(1918)],A=d,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,d,M,y,E){"use strict";var C=e(67971);B(o,"__esModule",{value:!0}),o.default=Ae,d=C(d),M=C(M),y=C(y),E=C(E);function Ae(T){function c(R,i){var s=T.onNodeAddClick;s&&s(R,i)}var n=T.data,p=T.editable,F=T.lineWidth,ee=T.padding;return y.default.createElement("div",{className:E.default.switchArrowContainer,style:{width:"100%",paddingLeft:ee}},y.default.createElement("div",{className:E.default.switchArrowDownContainer},y.default.createElement("div",{className:E.default.switchArrowDown}),y.default.createElement(d.default,{className:E.default.switchArrowDownIcon,type:"caret-down",size:"small"})),y.default.createElement("div",{className:E.default.switchArrowLine,style:{width:F}}),y.default.createElement("div",{className:E.default.switchArrowUpContainer},y.default.createElement("div",{className:E.default.switchArrowUp}),p&&y.default.createElement(M.default,{className:E.default.switchArrowIcon,type:"icon-tianjia2",onClick:function(){return c(n,n.next)}})))}})},9577:function(V,f,e){var A,x,u,B=e(24596),o=e(67394),d=e(93168),M=e(23587);(function(y,E){if(!0)!(x=[f,e(72153),e(17225),e(57379),e(81853),e(63835),e(27378),e(66697),e(98784),e(60042),e(14798),e(1918),e(17047),e(17640)],A=E,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(y,E,C,Ae,T,c,n,p,F,ee,R,i,s,ie){"use strict";var l=e(67971);o(y,"__esModule",{value:!0}),y.default=k,E=l(E),C=l(C),Ae=l(Ae),T=l(T),c=l(c),n=Z(n),p=l(p),F=l(F),ee=l(ee),R=l(R),i=l(i);function ne(t){if(typeof d!="function")return null;var H=new d,_=new d;return(ne=function(_e){return _e?_:H})(t)}function Z(t,H){if(!H&&t&&t.__esModule)return t;if(t===null||B(t)!=="object"&&typeof t!="function")return{default:t};var _=ne(H);if(_&&_.has(t))return _.get(t);var $={},_e=o&&M;for(var K in t)if(K!=="default"&&Object.prototype.hasOwnProperty.call(t,K)){var se=_e?M(t,K):null;se&&(se.get||se.set)?o($,K,se):$[K]=t[K]}return $.default=t,_&&_.set(t,$),$}var te={prepare:"#FFDDB2",attack:"#79B3F3",check:"#BAB1EA",recover:"#2A828A"},me={0:{tag:"#EDF7ED",word:"#629962"},1:{tag:"#EDF3F7",word:"#628099"},2:{tag:"#F0EDF7",word:"#746299"},3:{tag:"#F7EDED",word:"#996262"},4:{tag:"#F7F6ED",word:"#999062"}};function k(t){var H=t.permission,_=(0,n.useState)(!1),$=(0,T.default)(_,2),_e=$[0],K=$[1],se=(0,n.useState)(!1),we=(0,T.default)(se,2),Te=we[0],Ie=we[1],We=(0,n.useState)(!1),be=(0,T.default)(We,2),a=be[0],P=be[1],m=function(){var N=t.data,j=t.onClick;F.default.isEmpty(N)||j&&j(N)},ae=function(N){N.stopPropagation();var j=t.data,de=t.onNodeDeleteClick;de&&de(j)},D=function(N,j){var de=t.onNodeAddClick;de&&de(N,j)},G=function(N){if(P(!0),N.stopPropagation(),!(0,ie.handleIsAdmin)(H,4))return;var j=t.data,de=t.onTryAgainClick;F.default.isEmpty(j)||de&&de(j,function(){P(!1)})},Ee=function(N,j){if(j?K(!0):Ie(!0),N.stopPropagation(),!(0,ie.handleIsAdmin)(H,4))return;var de=t.data,he=t.onCheck;F.default.isEmpty(de)||he&&he(j,de,function(){j?K(!1):Ie(!1)})};function Re(){var L=t.data,N=!1;if(!F.default.isEmpty(L)){var j=L.arguments||L.args;F.default.isEmpty(j)||j.forEach(function(de){var he;(he=de.argumentList)===null||he===void 0||he.forEach(function(pe){var Le=pe.component;F.default.isEmpty(Le)||Le.required&&(N=!0)})})}return N}var O=t.isExpertise,w=t.editable,v=t.data,Ne=t.isHead,xe=t.isLineFirst,ve=t.isLineLast,De=t.isLast,ge=t.selected,Me=v.stage,Pe=v.activityName,Oe=v.name,Se=v.miniAppName,oe=v.state,Q=v.runResult,Fe=v.retryable,Y=v.userCheckState,r=v.nodeType,z=v.hostPercent,ue=v.hosts,re=Pe||Oe||Se,I=Re(),J=(0,Ae.default)({},i.default.nodeBox,!0),ke=t.hasOwnProperty("deletable")?t.deletable:v.deletable,Be,fe;r!==s.NORMAL&&r!==s.TASK?(fe=null,Be=i.default.globalNodeContainer):(fe=w?i.default.tailBox:i.default.tailBoxReadOnly,Be=i.default.normalNodeContainer),ge&&(J[i.default.selectedNode]=!0),v.hasOwnProperty("argsValid")&&(w&&!v.argsValid&&!O&&(J[i.default.selectedNode]=!1,J[i.default.errorNode]=!0)),oe==="RUNNING"&&!ge?J[i.default.activityRunning]=!0:oe==="FINISHED"&&!ge&&(Q&&Q===s.NodeRunResult.SUCCESS?J[i.default.activitySuccess]=!0:Q&&Q===s.NodeRunResult.REJECTED?J[i.default.activityRejected]=!0:J[i.default.activityFailed]=!0);function ce(L){return me[L%5].tag||""}function Ke(L){return me[L%5].word||""}return n.default.createElement("div",{className:Be},w&&Ne&&n.default.createElement("div",{className:i.default.headBox},n.default.createElement(c.default,{className:i.default.iconHead,type:"icon-tianjia2",onClick:function(){return D(null,v)},"data-autolog":"text=".concat(R.default.t("Add a walkthrough node"))}),n.default.createElement("div",{className:i.default.arrowLineHead})),xe&&w&&n.default.createElement("div",{className:i.default.firstBox}),n.default.createElement("div",{title:Pe||Oe||Se,className:(0,ee.default)(J),onClick:m},n.default.createElement("div",{className:i.default.topLayer},n.default.createElement("div",{className:i.default.topLeftBox},n.default.createElement("div",{className:i.default.stageIcon,style:{backgroundColor:te[Me]}}),n.default.createElement("span",{title:re,className:i.default.nodeName},re)),n.default.createElement("div",{className:i.default.topRightBox},w&&!O&&v.argsValid===!1&&n.default.createElement(C.default,{type:"delete-filling",size:"xs",style:{color:"#D93026",marginRight:12}}),Q&&Q===s.NodeRunResult.SUCCESS&&n.default.createElement(C.default,{type:"success-filling",size:"small",style:{color:"#1E8E3E",marginRight:12}}),Q&&F.default.indexOf(s.NodeRunResult.FAILED,Q)>=0&&n.default.createElement(C.default,{type:"delete-filling",size:"small",style:{color:"#D93026",marginRight:12}}),oe==="RUNNING"&&n.default.createElement(C.default,{type:"loading",size:"small",style:{marginRight:12}}),!w&&v.groupOrder&&n.default.createElement("div",{className:i.default.groupOrderBox,style:{backgroundColor:ce(v.groupOrder)}},n.default.createElement("span",{style:{color:Ke(v.groupOrder)}},"\u5206\u7EC4",v.groupOrder)))),n.default.createElement("div",{className:i.default.bottomLayer},n.default.createElement("div",{className:i.default.bottomLeftBox},n.default.createElement("div",{className:i.default.parameterInfo},!O&&w&&!oe&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,n.default.createElement(p.default,null,"Required parameters"),": "),(I||r===s.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},n.default.createElement(p.default,null,"Have")),!I&&r!==s.RECOVER&&n.default.createElement("span",null,n.default.createElement(p.default,null,"None"))),!O&&!w&&!oe&&r!==s.NORMAL&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,n.default.createElement(p.default,null,"Required parameters"),": "),(I||r===s.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},n.default.createElement(p.default,null,"Have")),!I&&r!==s.RECOVER&&n.default.createElement(p.default,null,"None")),O&&!w&&!oe&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,n.default.createElement(p.default,null,"Required parameters"),": "),(I||r===s.RECOVER)&&n.default.createElement("span",{style:{color:"#D93026"}},n.default.createElement(p.default,null,"Have")),!I&&r!==s.RECOVER&&n.default.createElement(p.default,null,"None")),!O&&!w&&r===s.NORMAL&&!oe&&n.default.createElement(n.default.Fragment,null,n.default.createElement("span",null,n.default.createElement(p.default,null,"Involving machines"),": "),z&&z!==0&&!ue?n.default.createElement("span",{style:{color:"#0070cc"}},z,"%"):n.default.createElement("span",null),ue&&n.default.createElement("span",{style:{color:"#0070cc"}},"".concat(v.hosts," ").concat(R.default.t("Number")))),Y===s.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING&&n.default.createElement("span",null,n.default.createElement(p.default,null,"To be pushed manually")),Q&&Q===s.NodeRunResult.SUCCESS&&n.default.createElement("span",null,n.default.createElement(p.default,null,"Node executes successfully")),Q&&F.default.indexOf(s.NodeRunResult.FAILED,Q)>=0&&n.default.createElement("span",null,n.default.createElement(p.default,null,"Node execution failed")),Q&&Q===s.NodeRunResult.REJECTED&&n.default.createElement("span",null,n.default.createElement(p.default,null,"Node execution is skipped")),oe&&oe==="RUNNING"&&n.default.createElement("span",null,n.default.createElement(p.default,null,"Node is executing...")))),n.default.createElement("div",{className:i.default.bottomRightBox},n.default.createElement("div",{className:i.default.userAction},Fe&&n.default.createElement("span",{className:i.default.contiueTryIcon,onClick:function(N){return G(N)}},n.default.createElement(E.default,{type:"primary",loading:a,text:!0,className:i.default.action,disabled:!(0,ie.handleIsAdmin)(H,4)},n.default.createElement(p.default,null,"Retry"))),Y===s.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING?n.default.createElement("span",{className:i.default.userCheck},n.default.createElement("span",{className:i.default.contiueTryIcon,onClick:function(N){return Ee(N,!0)}},n.default.createElement(E.default,{type:"primary",loading:_e,text:!0,className:i.default.action,disabled:!(0,ie.handleIsAdmin)(H,4)},n.default.createElement(p.default,null,"Continue"))),n.default.createElement("span",{onClick:function(N){return Ee(N,!1)}},n.default.createElement(E.default,{type:"primary",loading:Te,text:!0,className:i.default.action,disabled:!(0,ie.handleIsAdmin)(H,4)},n.default.createElement(p.default,null,"Termination")))):n.default.createElement("span",null)))),oe?ge&&n.default.createElement("div",{className:i.default.selectedTriangle}):ge&&v.argsValid&&n.default.createElement("div",{className:i.default.selectedTriangle}),w&&ke&&!oe&&n.default.createElement(C.default,{type:"ashbin",size:"small",style:{position:"absolute",top:16,right:12,color:"#888"},onClick:function(N){return ae(N)},"data-autolog":"text=".concat(R.default.t("Delete walkthrough node"))}),oe&&oe==="RUNNING"&&n.default.createElement("div",null)),!ve&&!De&&n.default.createElement("div",{className:fe},r===s.NORMAL||r===s.TASK?n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:i.default.arrowLineTail}),n.default.createElement(C.default,{type:"caret-right",size:"small"})):null,r===s.NORMAL&&w&&n.default.createElement(c.default,{className:i.default.iconTail,type:"icon-tianjia2",onClick:function(){return D(v,v.next)},"data-autolog":"text=".concat(R.default.t("Add a walkthrough node"))})),De&&w&&n.default.createElement("div",{className:fe},r===s.NORMAL||r===s.TASK?n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:i.default.arrowLineTail}),n.default.createElement(C.default,{type:"caret-right",size:"small"})):null,n.default.createElement(c.default,{className:i.default.iconTail,type:"icon-tianjia2",onClick:function(){return D(v,v.next)},"data-autolog":"text=".concat(R.default.t("Add a walkthrough node"))})))}})},17640:function(V,f,e){var A,x,u,B=e(67394);(function(o,d){if(!0)!(x=[f],A=d,u=typeof A=="function"?A.apply(f,x):A,u!==void 0&&(V.exports=u));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o){"use strict";B(o,"__esModule",{value:!0}),o.handleIsAdmin=void 0;var d=function(y,E){return(y&E)===E};o.handleIsAdmin=d})},94539:(V,f,e)=>{"use strict";e.d(f,{Z:()=>d});var A=e(60994),x=e.n(A),u=e(93476),B=e.n(u),o=B()(x());o.push([V.id,`.index__container__-R1ZY {
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
`],sourceRoot:""}]),o.locals={container:"index__container__-R1ZY",searchBox:"index__searchBox__4itYE",icon:"index__icon__Yrchm",search:"index__search__PJKPg",contentBox:"index__contentBox__lWDuC",categoryBox:"index__categoryBox__PsQka",categoryList:"index__categoryList__8lpnx",selectedCate:"index__selectedCate__fNyLs",clearSearch:"index__clearSearch__GLS+S",listBox:"index__listBox__2s+St",functionList:"index__functionList__8+lTI",selectedFunc:"index__selectedFunc__SnSVV",loading:"index__loading__IvmoD",descriptionBox:"index__descriptionBox__KVOZB",btnRow:"index__btnRow__f0+Nv",btn:"index__btn__5PaRI"};const d=o},87223:(V,f,e)=>{"use strict";e.d(f,{Z:()=>d});var A=e(60994),x=e.n(A),u=e(93476),B=e.n(u),o=B()(x());o.push([V.id,`.index__boxContainer__yI63h, .index__boxContainerReadOnly__rLzIJ {
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
}`],sourceRoot:""}]),o.locals={boxContainer:"index__boxContainer__yI63h",boxContainerReadOnly:"index__boxContainerReadOnly__rLzIJ",box:"index__box__9KijU",deleteFlowBox:"index__deleteFlowBox__AXAIQ"};const d=o},7137:(V,f,e)=>{"use strict";e.d(f,{Z:()=>d});var A=e(60994),x=e.n(A),u=e(93476),B=e.n(u),o=B()(x());o.push([V.id,`.index__normalNodeContainer__Vy6PM,
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
}`],sourceRoot:""}]),o.locals={normalNodeContainer:"index__normalNodeContainer__Vy6PM",globalNodeContainer:"index__globalNodeContainer__c2iqs",nodeBox:"index__nodeBox__L575N",topLayer:"index__topLayer__YoyPy",bottomLayer:"index__bottomLayer__EGcfl",selectedNode:"index__selectedNode__NNk6g",errorNode:"index__errorNode__SEDtu",selectedTriangle:"index__selectedTriangle__Oqr+U",Icon:"index__Icon__xQT+I",topLeftBox:"index__topLeftBox__9moQS",topRightBox:"index__topRightBox__4A70p",bottomLeftBox:"index__bottomLeftBox__nTv+6",bottomRightBox:"index__bottomRightBox__RUa5D",stageIcon:"index__stageIcon__tpXvL",nodeName:"index__nodeName__wTYa9",parameterInfo:"index__parameterInfo__96vV4",firstBox:"index__firstBox__pQ99F",headBox:"index__headBox__qFHO8",tailBox:"index__tailBox__9yi1O",iconHead:"index__iconHead__vySzz",iconTail:"index__iconTail__LXrO0",switchArrowIcon:"index__switchArrowIcon__FaGZ0",tailBoxReadOnly:"index__tailBoxReadOnly__0sFpl",circle:"index__circle__vnKfM",lastCircle:"index__lastCircle__2Ijzo",arrowLineHead:"index__arrowLineHead__IKRb5",arrowLineTail:"index__arrowLineTail__Qew1w",switchArrowContainer:"index__switchArrowContainer__g-hjn",switchArrowDownContainer:"index__switchArrowDownContainer__YPtrF",switchArrowDown:"index__switchArrowDown__9Y1gq",switchArrowUp:"index__switchArrowUp__S4BW8",switchArrowDownIcon:"index__switchArrowDownIcon__zWpXt",switchArrowLine:"index__switchArrowLine__wWH4C",switchArrowUpContainer:"index__switchArrowUpContainer__6UWN4",groupOrderBox:"index__groupOrderBox__3NNud",action:"index__action__MfFWc",userCheck:"index__userCheck__yrAUG",userAction:"index__userAction__dOpzq",contiueTryIcon:"index__contiueTryIcon__19jn4",activityRunning:"index__activityRunning__zKrBm",activityRejected:"index__activityRejected__GBzVm",activitySuccess:"index__activitySuccess__p4-Rg",activityFailed:"index__activityFailed__RckCx"};const d=o},54581:(V,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>d});var A=e(1892),x=e.n(A),u=e(94539),B={};B.insert="head",B.singleton=!1;var o=x()(u.Z,B);const d=u.Z.locals||{}},39725:(V,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>d});var A=e(1892),x=e.n(A),u=e(87223),B={};B.insert="head",B.singleton=!1;var o=x()(u.Z,B);const d=u.Z.locals||{}},1918:(V,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>d});var A=e(1892),x=e.n(A),u=e(7137),B={};B.insert="head",B.singleton=!1;var o=x()(u.Z,B);const d=u.Z.locals||{}}}]);

//# sourceMappingURL=313.bundle.js.map