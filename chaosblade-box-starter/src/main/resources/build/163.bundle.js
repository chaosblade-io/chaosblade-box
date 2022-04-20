(self.webpackChunk=self.webpackChunk||[]).push([[163],{4163:function(O,m,e){var _,x,f,p=e(24596),l=e(67394),A=e(93168),L=e(23587),N=e(83452),U=e(95315),R=e(63774),j=e(92937);(function(F,r){if(!0)!(x=[m,e(77809),e(81853),e(57379),e(91714),e(47701),e(87286),e(48492),e(94518),e(27378),e(68250),e(93525),e(98784),e(7609),e(96291),e(99328),e(14870),e(42058)],_=r,f=typeof _=="function"?_.apply(m,x):_,f!==void 0&&(O.exports=f));else var D})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(F,r,D,B,v,K,X,$,Y,i,n,I,d,c,S,h,y,G){"use strict";var g=e(67971);l(F,"__esModule",{value:!0}),F.default=void 0,r=g(r),D=g(D),B=g(B),v=g(v),K=g(K),X=g(X),$=g($),Y=g(Y),i=k(i),n=g(n),I=g(I),d=g(d),c=g(c);function Q(t){if(typeof A!="function")return null;var a=new A,o=new A;return(Q=function(T){return T?o:a})(t)}function k(t,a){if(!a&&t&&t.__esModule)return t;if(t===null||p(t)!=="object"&&typeof t!="function")return{default:t};var o=Q(a);if(o&&o.has(t))return o.get(t);var u={},T=l&&L;for(var W in t)if(W!=="default"&&Object.prototype.hasOwnProperty.call(t,W)){var Z=T?L(t,W):null;Z&&(Z.get||Z.set)?l(u,W,Z):u[W]=t[W]}return u.default=t,o&&o.set(t,u),u}function J(t,a){var o=N(t);if(U){var u=U(t);a&&(u=u.filter(function(T){return L(t,T).enumerable})),o.push.apply(o,u)}return o}function H(t){for(var a=1;a<arguments.length;a++){var o=arguments[a]!=null?arguments[a]:{};a%2?J(Object(o),!0).forEach(function(u){(0,B.default)(t,u,o[u])}):R?j(t,R(o)):J(Object(o)).forEach(function(u){l(t,u,L(o,u))})}return t}var w=K.default.Item,M=v.default.Item,P=function(){var a=(0,y.useDispatch)(),o=(0,G.useHistory)(),u=(0,y.useSelector)(function(E){return H(H({},E.experimentEditor),E.experimentDataSource)}),T=u.experiment,W=u.tags,Z=(0,i.useState)(0),ae=(0,D.default)(Z,2),z=ae[0],re=ae[1],_e=(0,i.useState)(null),ie=(0,D.default)(_e,2),oe=ie[0],fe=ie[1],Ee=(0,i.useState)(!1),ue=(0,D.default)(Ee,2),ee=ue[0],se=ue[1],b=(0,h.parseQuery)();(0,i.useEffect)(function(){if(a.pageHeader.setTitle("\u6F14\u7EC3\u914D\u7F6E"),a.pageHeader.setBreadCrumbItems(S.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace",value:"\u7A7A\u95F4\u7BA1\u7406",path:"/chaos/workspace/list"},{key:"experiment_editor",value:"\u6F14\u7EC3\u914D\u7F6E",path:"/chaos/experiment/edtior"}])),!d.default.isEmpty(b)){a.experimentEditor.setClearExperiment();var E=b.id,V=b.expertiseId,ne=b.code;d.default.isEmpty(E)?V?(a.experimentEditor.setClearExperiment(),(0,r.default)(regeneratorRuntime.mark(function C(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentByExpertise({expertise_id:V});case 2:case"end":return s.stop()}},C)}))()):ne&&(a.experimentEditor.setClearExperiment(),(0,r.default)(regeneratorRuntime.mark(function C(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentByAppCode({appCode:ne});case 2:case"end":return s.stop()}},C)}))()):(fe(E),se(!0),(0,r.default)(regeneratorRuntime.mark(function C(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentBaseInfo({experimentId:E});case 2:case"end":return s.stop()}},C)}))(),(0,r.default)(regeneratorRuntime.mark(function C(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperiment({experimentId:E},function(Ce){var Be=Ce.flowInfo;d.default.isEmpty(Be)||se(!0)});case 2:case"end":return s.stop()}},C)}))())}},[]);function me(){return i.default.createElement($.default,{baseInfo:d.default.get(T,"baseInfo",{}),onEditExperimentBaseInfo:pe})}function pe(){(0,h.pushUrl)(o,"/chaos/baseInfo/editor",{pass:"detail"})}function Ae(E){a.experimentEditor.setUpdateBaseInfo(E)}function ce(E){(0,r.default)(regeneratorRuntime.mark(function V(){return regeneratorRuntime.wrap(function(C){for(;;)switch(C.prev=C.next){case 0:return C.next=2,a.experimentDataSource.getTags(E);case 2:case"end":return C.stop()}},V)}))()}function ge(){ce({key:"",type:0})}function le(){var E=z+1;re(E>2?2:E)}function xe(){var E=z-1;re(E<0?0:E)}function de(){a.experimentEditor.setClearExperiment(),(0,h.pushUrl)(o,"/chaos/experiment/detail")}function he(){if(z===0)return i.default.createElement(n.default,{isEdit:ee,onNext:le,onBack:de,isExpertise:!1});if(z===1)return i.default.createElement(I.default,{experimentId:oe,isEdit:ee,onNext:le,onPrev:xe,onBack:de,isExpertise:!1})}return i.default.createElement("div",{className:c.default.experimentEditor},ee?me():i.default.createElement(X.default,{onUpdateBasinfo:Ae,tags:W,data:d.default.get(T,"baseInfo",{}),disabled:z===2,onFocusTags:ge}),i.default.createElement(K.default,{className:c.default.tabs,shape:"wrapped"},i.default.createElement(w,{title:"\u914D\u7F6E"},i.default.createElement("div",{className:c.default.configureItem},i.default.createElement("div",null,i.default.createElement(v.default,{current:z,shape:"circle",labelPlacement:"hoz",className:c.default.steps},i.default.createElement(M,{title:"\u6F14\u7EC3\u5BF9\u8C61",content:"\u5E94\u7528\u548C\u6545\u969C"}),i.default.createElement(M,{title:"\u5168\u5C40\u914D\u7F6E",content:"\u5168\u5C40\u53C2\u6570\u8BBE\u7F6E"})),he()))),i.default.createElement(w,{title:"\u8BB0\u5F55"},i.default.createElement(Y.default,{experimentId:oe}))))},q=P;F.default=q})},87286:function(O,m,e){var _,x,f,p=e(24596),l=e(67394),A=e(93168),L=e(23587);(function(N,U){if(!0)!(x=[m,e(28757),e(8583),e(15286),e(81853),e(27378),e(98784),e(17268)],_=U,f=typeof _=="function"?_.apply(m,x):_,f!==void 0&&(O.exports=f));else var R})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(N,U,R,j,F,r,D,B){"use strict";var v=e(67971);l(N,"__esModule",{value:!0}),N.default=void 0,U=v(U),R=v(R),j=v(j),F=v(F),r=X(r),D=v(D),B=v(B);function K(n){if(typeof A!="function")return null;var I=new A,d=new A;return(K=function(S){return S?d:I})(n)}function X(n,I){if(!I&&n&&n.__esModule)return n;if(n===null||p(n)!=="object"&&typeof n!="function")return{default:n};var d=K(I);if(d&&d.has(n))return d.get(n);var c={},S=l&&L;for(var h in n)if(h!=="default"&&Object.prototype.hasOwnProperty.call(n,h)){var y=S?L(n,h):null;y&&(y.get||y.set)?l(c,h,y):c[h]=n[h]}return c.default=n,d&&d.set(n,c),c}var $={labelCol:{span:2},wrapperCol:{span:6}};function Y(n){var I=(0,r.useState)(!1),d=(0,F.default)(I,2),c=d[0],S=d[1],h=(0,r.useState)(!1),y=(0,F.default)(h,2),G=y[0],g=y[1];function Q(P){w("name",P)}function k(P){w("description",P)}function J(P){if(!D.default.isEmpty(P)&&P.length>5){S(!0);return}w("tags",P),S(!1),g(!1)}function H(){return c?"\u6700\u591A\u586B\u51995\u4E2A\uFF01":G?"1\u4E2A\u6807\u7B7E\u6700\u591A\u5305\u542B30\u4E2A\u5B57\u7B26\uFF01":""}function w(P,q){var t=n.data,a=n.onUpdateBasinfo;t[P]=q,a(t)}var M=n.data;return r.default.createElement("div",{className:B.default.warp},r.default.createElement(R.default,$,r.default.createElement(R.default.Item,{label:"\u6F14\u7EC3\u540D\u79F0",required:!0,requiredTrigger:"onBlur"},r.default.createElement(j.default,{value:M&&M.name||"",className:B.default.experienceBase,maxLength:50,showLimitHint:!0,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u540D\u79F0",name:"\u6F14\u7EC3\u540D\u79F0",onChange:Q,disabled:n.disabled,"data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u540D\u79F0"})),r.default.createElement(R.default.Item,{label:"\u6F14\u7EC3\u63CF\u8FF0"},r.default.createElement(j.default.TextArea,{value:M&&M.description,className:B.default.experienceBaseDescribe,maxLength:1e3,showLimitHint:!0,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u63CF\u8FF0\u4FE1\u606F",onChange:k,disabled:n.disabled,"data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u63CF\u8FF0"})),r.default.createElement(R.default.Item,{label:"\u6F14\u7EC3\u6807\u7B7E"},r.default.createElement(U.default,{value:M&&M.tags,className:B.default.drillTag,onChange:J,showSearch:!0,dataSource:n.tags,mode:"tag",disabled:n.disabled,onFocus:n.onFocusTags,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u6807\u7B7E",notFoundContent:"\u624B\u52A8\u8F93\u5165\u540E\u70B9\u51FB\u56DE\u8F66\u6DFB\u52A0","data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u6807\u7B7E"}),H()&&r.default.createElement("div",{className:B.default.errorMessage},H()))))}var i=Y;N.default=i})},82239:(O,m,e)=>{"use strict";e.d(m,{Z:()=>A});var _=e(60994),x=e.n(_),f=e(93476),p=e.n(f),l=p()(x());l.push([O.id,`.index__experimentEditor__eFdlm {
  padding:16px 0px;
}

.index__tabs__x28i0{
  margin-top: 16px;
}

.index__tabs__x28i0 .index__configureItem__dZd7I {
  padding: 24px 24px 24px 0;
}

.index__configureItem__dZd7I .index__noConfigContainer__Y5f7- {
  display: flex;
  align-items: center;

}

.index__configureItem__dZd7I .index__noConfigContainer__Y5f7- img {
  width: 213px;
  height: 150px;
}

.index__configureItem__dZd7I .index__noConfigBox__TwKXb {
  margin-left: 40px;
  height: 104px;

}

.index__configureItem__dZd7I .index__noConfigBox__TwKXb .index__customCreate__5azRq {
  margin-left: 8px;
}

.index__configureItem__dZd7I .index__noConfig__08sM5 {
  font-size: 18px;
  color: #111;
  font-weight: bold;
  line-height: 24px;
}

.index__configureItem__dZd7I .index__noConfigTips__j9aTu {
  margin-top: 8px;
  margin-bottom: 24px;
  font-size: 12px;
  color: #333;
  line-height: 16px;
}

.index__steps__op-Af {
  text-align: justify !important;
  margin-bottom: 21px;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentEditor/index.css"],names:[],mappings:"AAAA;EACE,gBAAgB;AAClB;;AAEA;EACE,gBAAgB;AAClB;;AAEA;EACE,yBAAyB;AAC3B;;AAEA;EACE,aAAa;EACb,mBAAmB;;AAErB;;AAEA;EACE,YAAY;EACZ,aAAa;AACf;;AAEA;EACE,iBAAiB;EACjB,aAAa;;AAEf;;AAEA;EACE,gBAAgB;AAClB;;AAEA;EACE,eAAe;EACf,WAAW;EACX,iBAAiB;EACjB,iBAAiB;AACnB;;AAEA;EACE,eAAe;EACf,mBAAmB;EACnB,eAAe;EACf,WAAW;EACX,iBAAiB;AACnB;;AAEA;EACE,8BAA8B;EAC9B,mBAAmB;AACrB",sourcesContent:[`.experimentEditor {
  padding:16px 0px;
}

.tabs{
  margin-top: 16px;
}

.tabs .configureItem {
  padding: 24px 24px 24px 0;
}

.configureItem .noConfigContainer {
  display: flex;
  align-items: center;

}

.configureItem .noConfigContainer img {
  width: 213px;
  height: 150px;
}

.configureItem .noConfigBox {
  margin-left: 40px;
  height: 104px;

}

.configureItem .noConfigBox .customCreate {
  margin-left: 8px;
}

.configureItem .noConfig {
  font-size: 18px;
  color: #111;
  font-weight: bold;
  line-height: 24px;
}

.configureItem .noConfigTips {
  margin-top: 8px;
  margin-bottom: 24px;
  font-size: 12px;
  color: #333;
  line-height: 16px;
}

.steps {
  text-align: justify !important;
  margin-bottom: 21px;
}
`],sourceRoot:""}]),l.locals={experimentEditor:"index__experimentEditor__eFdlm",tabs:"index__tabs__x28i0",configureItem:"index__configureItem__dZd7I",noConfigContainer:"index__noConfigContainer__Y5f7-",noConfigBox:"index__noConfigBox__TwKXb",customCreate:"index__customCreate__5azRq",noConfig:"index__noConfig__08sM5",noConfigTips:"index__noConfigTips__j9aTu",steps:"index__steps__op-Af"};const A=l},424:(O,m,e)=>{"use strict";e.d(m,{Z:()=>A});var _=e(60994),x=e.n(_),f=e(93476),p=e.n(f),l=p()(x());l.push([O.id,`.index__warp__h61oO {
  width: 100%;
  padding-right: 25px;

}

.index__experienceBase__JMFre{
  width: 520px !important;
}

.index__experienceBaseDescribe__rQAtr{
  width: 520px !important;
}

.index__drillTag__fAFBO{
  width: 520px !important;
}

.index__submit__Mxz1b{
  margin-top: 40px;
  border-top: 1px solid #DEDEDE;
  padding-top: 16px;
}

.index__warp__h61oO.next-form-item-control{
  display: inline-block !important;
}

.index__errorMessage__\\+7buf {
  width: 100%;
  height: 18px;
  font-size: 12px;
  color: #f5222d;
  margin-top: 3px;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/common/BaseInfo/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,mBAAmB;;AAErB;;AAEA;EACE,uBAAuB;AACzB;;AAEA;EACE,uBAAuB;AACzB;;AAEA;EACE,uBAAuB;AACzB;;AAEA;EACE,gBAAgB;EAChB,6BAA6B;EAC7B,iBAAiB;AACnB;;AAEA;EACE,gCAAgC;AAClC;;AAEA;EACE,WAAW;EACX,YAAY;EACZ,eAAe;EACf,cAAc;EACd,eAAe;AACjB",sourcesContent:[`.warp {
  width: 100%;
  padding-right: 25px;

}

.experienceBase{
  width: 520px !important;
}

.experienceBaseDescribe{
  width: 520px !important;
}

.drillTag{
  width: 520px !important;
}

.submit{
  margin-top: 40px;
  border-top: 1px solid #DEDEDE;
  padding-top: 16px;
}

.warp:global(.next-form-item-control){
  display: inline-block !important;
}

.errorMessage {
  width: 100%;
  height: 18px;
  font-size: 12px;
  color: #f5222d;
  margin-top: 3px;
}
`],sourceRoot:""}]),l.locals={warp:"index__warp__h61oO",experienceBase:"index__experienceBase__JMFre",experienceBaseDescribe:"index__experienceBaseDescribe__rQAtr",drillTag:"index__drillTag__fAFBO",submit:"index__submit__Mxz1b",errorMessage:"index__errorMessage__+7buf"};const A=l},7609:(O,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>A});var _=e(1892),x=e.n(_),f=e(82239),p={};p.insert="head",p.singleton=!1;var l=x()(f.Z,p);const A=f.Z.locals||{}},17268:(O,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>A});var _=e(1892),x=e.n(_),f=e(424),p={};p.insert="head",p.singleton=!1;var l=x()(f.Z,p);const A=f.Z.locals||{}}}]);

//# sourceMappingURL=163.bundle.js.map