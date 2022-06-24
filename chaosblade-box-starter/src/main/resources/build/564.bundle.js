(self.webpackChunk=self.webpackChunk||[]).push([[564],{8564:function(S,m,e){var i,A,u,_=e(24596),s=e(67394),E=e(93168),w=e(23587),$=e(83452),L=e(95315),P=e(63774),X=e(92937);(function(O,o){if(!0)!(A=[m,e(72153),e(17534),e(57379),e(77809),e(81853),e(87286),e(27378),e(66697),e(98784),e(14798),e(68149),e(96291),e(99328),e(14870),e(42058)],i=o,u=typeof i=="function"?i.apply(m,A):i,u!==void 0&&(S.exports=u));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(O,o,F,l,y,x,g,p,z,j,M,K,n,v,h,R){"use strict";var f=e(67971);s(O,"__esModule",{value:!0}),O.default=void 0,o=f(o),F=f(F),l=f(l),y=f(y),x=f(x),g=f(g),p=I(p),z=f(z),j=f(j),M=f(M),K=f(K);function B(t){if(typeof E!="function")return null;var d=new E,r=new E;return(B=function(c){return c?r:d})(t)}function I(t,d){if(!d&&t&&t.__esModule)return t;if(t===null||_(t)!=="object"&&typeof t!="function")return{default:t};var r=B(d);if(r&&r.has(t))return r.get(t);var a={},c=s&&w;for(var D in t)if(D!=="default"&&Object.prototype.hasOwnProperty.call(t,D)){var U=c?w(t,D):null;U&&(U.get||U.set)?s(a,D,U):a[D]=t[D]}return a.default=t,r&&r.set(t,a),a}function Z(t,d){var r=$(t);if(L){var a=L(t);d&&(a=a.filter(function(c){return w(t,c).enumerable})),r.push.apply(r,a)}return r}function J(t){for(var d=1;d<arguments.length;d++){var r=arguments[d]!=null?arguments[d]:{};d%2?Z(Object(r),!0).forEach(function(a){(0,l.default)(t,a,r[a])}):P?X(t,P(r)):Z(Object(r)).forEach(function(a){s(t,a,w(r,a))})}return t}function Y(){var t=(0,h.useDispatch)(),d=(0,R.useHistory)(),r=(0,h.useSelector)(function(C){var W=C.experimentDetail;return W.baseInfo}),a=(0,h.useSelector)(function(C){var W=C.experimentDataSource;return W.tags}),c=(0,p.useState)(null),D=(0,x.default)(c,2),U=D[0],V=D[1];(0,p.useEffect)(function(){var C=(0,v.parseQuery)(),W=j.default.get(C,"id","");W&&(0,y.default)(regeneratorRuntime.mark(function N(){return regeneratorRuntime.wrap(function(T){for(;;)switch(T.prev=T.next){case 0:return T.next=2,t.experimentDetail.getExperimentBaseInfo({experimentId:W},function(H){return V(function(){var G=H.workspaces,ae=G===void 0?[]:G,b=[];return j.default.forEach(ae,function(re){return b.push(re.workspaceId)}),J(J({},H),{},{workspaces:b})})});case 2:case"end":return T.stop()}},N)}))()},[]),(0,p.useEffect)(function(){t.pageHeader.setTitle(M.default.t("Basic information about the drill").toString()),t.pageHeader.setBreadCrumbItems(n.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace",value:M.default.t("Space management").toString(),path:"/chaos/workspace/list"},{key:"experiment_detail",value:M.default.t("Drill details").toString(),path:"/chaos/experiment/detail"},{key:"experiment_baseInfo",value:M.default.t("Basic information about the drill").toString(),path:"/chaos/baseInfo/editor"}]))});function k(C){t.experimentDetail.setUpdateBaseInfo(C)}function q(){var C=(0,v.parseQuery)(),W=j.default.get(C,"id","");(0,y.default)(regeneratorRuntime.mark(function N(){return regeneratorRuntime.wrap(function(T){for(;;)switch(T.prev=T.next){case 0:return T.next=2,t.experimentDetail.updateExperimentBasicInfo(J(J({},r),{},{experimentId:W}),function(H){H&&F.default.success(M.default.t("Update completed").toString()),(0,v.pushUrl)(d,"/chaos/experiment/detail")});case 2:case"end":return T.stop()}},N)}))()}function ee(){t.experimentEditor.setClearExperiment(),(0,v.pushUrl)(d,"/chaos/experiment/detail")}function te(){(0,y.default)(regeneratorRuntime.mark(function C(){return regeneratorRuntime.wrap(function(N){for(;;)switch(N.prev=N.next){case 0:return N.next=2,t.experimentDataSource.getTags({key:"",type:0});case 2:case"end":return N.stop()}},C)}))()}return p.default.createElement("div",{className:K.default.container},p.default.createElement(g.default,{data:U,tags:a,disabled:!1,onUpdateBasinfo:k,onFocusTags:te}),p.default.createElement("div",{className:K.default.Divider}),p.default.createElement("div",null,p.default.createElement(o.default,{className:K.default.headerButton,type:"primary",onClick:q},p.default.createElement(z.default,null,"Confirm")),p.default.createElement(o.default,{className:K.default.headerButton,onClick:ee},p.default.createElement(z.default,null,"Cancel and return to details"))))}var Q=Y;O.default=Q})},87286:function(S,m,e){var i,A,u,_=e(24596),s=e(67394),E=e(93168),w=e(23587);(function($,L){if(!0)!(A=[m,e(28757),e(8583),e(15286),e(81853),e(27378),e(98784),e(14798),e(68055),e(17268)],i=L,u=typeof i=="function"?i.apply(m,A):i,u!==void 0&&(S.exports=u));else var P})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function($,L,P,X,O,o,F,l,y,x){"use strict";var g=e(67971);s($,"__esModule",{value:!0}),$.default=void 0,L=g(L),P=g(P),X=g(X),O=g(O),o=z(o),F=g(F),l=g(l),y=g(y),x=g(x);function p(n){if(typeof E!="function")return null;var v=new E,h=new E;return(p=function(f){return f?h:v})(n)}function z(n,v){if(!v&&n&&n.__esModule)return n;if(n===null||_(n)!=="object"&&typeof n!="function")return{default:n};var h=p(v);if(h&&h.has(n))return h.get(n);var R={},f=s&&w;for(var B in n)if(B!=="default"&&Object.prototype.hasOwnProperty.call(n,B)){var I=f?w(n,B):null;I&&(I.get||I.set)?s(R,B,I):R[B]=n[B]}return R.default=n,h&&h.set(n,R),R}var j={labelCol:{span:2},wrapperCol:{span:6}};function M(n){var v=(0,o.useState)(!1),h=(0,O.default)(v,2),R=h[0],f=h[1],B=(0,o.useState)(!1),I=(0,O.default)(B,2),Z=I[0],J=I[1];function Y(c){r("name",c)}function Q(c){r("description",c)}function t(c){if(!F.default.isEmpty(c)&&c.length>5){f(!0);return}r("tags",c),f(!1),J(!1)}function d(){return R?l.default.t("Fill up to 5"):Z?l.default.t("1 label can contain up to 30 characters"):""}function r(c,D){var U=n.data,V=n.onUpdateBasinfo;U[c]=D,V(U)}var a=n.data;return o.default.createElement("div",{className:x.default.warp},o.default.createElement(P.default,j,o.default.createElement(P.default.Item,{label:l.default.t("Drill name").toString(),required:!0,requiredTrigger:"onBlur"},o.default.createElement(X.default,{value:a&&a.name||"",className:x.default.experienceBase,maxLength:50,showLimitHint:!0,placeholder:l.default.t("Please enter a drill name").toString(),name:l.default.t("Drill name"),onChange:Y,disabled:n.disabled,"data-autolog":"text=".concat(l.default.t("Please enter a drill name").toString())})),o.default.createElement(P.default.Item,{label:l.default.t("Walkthrough Description").toString()},o.default.createElement(X.default.TextArea,{value:a&&a.description,className:x.default.experienceBaseDescribe,maxLength:1e3,showLimitHint:!0,placeholder:l.default.t("Please enter a walkthrough description").toString(),onChange:Q,disabled:n.disabled,"data-autolog":"text=".concat(l.default.t("Please enter a walkthrough description").toString())})),o.default.createElement(P.default.Item,{label:l.default.t("Walkthrough Tags").toString()},o.default.createElement(L.default,{value:a&&a.tags,className:x.default.drillTag,onChange:t,showSearch:!0,dataSource:n.tags,mode:"tag",disabled:n.disabled,onFocus:n.onFocusTags,placeholder:l.default.t("Please enter a walkthrough tag").toString(),notFoundContent:l.default.t("After entering manually, click Enter to add").toString(),"data-autolog":"text=".concat(l.default.t("Enter a walkthrough tag")),locale:(0,y.default)().Select}),d()&&o.default.createElement("div",{className:x.default.errorMessage},d()))))}var K=M;$.default=K})},26086:(S,m,e)=>{"use strict";e.d(m,{Z:()=>E});var i=e(60994),A=e.n(i),u=e(93476),_=e.n(u),s=_()(A());s.push([S.id,`.index__container__EJi4F {
  padding: 20px 17px 0 17px;
}

.index__headerButton__8\\+VJw:last-child {
    margin-left: 8px;
  }

.index__Divider__JcOS5 {
  width: 100%;
  height: 1px;
  background: #e8e8e8;
  margin: 16px 0;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentEditor/BaseInfoEditor/index.css"],names:[],mappings:"AAAA;EACE,yBAAyB;AAC3B;;AAGE;IACE,gBAAgB;EAClB;;AAGF;EACE,WAAW;EACX,WAAW;EACX,mBAAmB;EACnB,cAAc;AAChB",sourcesContent:[`.container {
  padding: 20px 17px 0 17px;
}

.headerButton {
  &:last-child {
    margin-left: 8px;
  }
}

.Divider {
  width: 100%;
  height: 1px;
  background: #e8e8e8;
  margin: 16px 0;
}
`],sourceRoot:""}]),s.locals={container:"index__container__EJi4F",headerButton:"index__headerButton__8+VJw",Divider:"index__Divider__JcOS5"};const E=s},424:(S,m,e)=>{"use strict";e.d(m,{Z:()=>E});var i=e(60994),A=e.n(i),u=e(93476),_=e.n(u),s=_()(A());s.push([S.id,`.index__warp__h61oO {
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
`],sourceRoot:""}]),s.locals={warp:"index__warp__h61oO",experienceBase:"index__experienceBase__JMFre",experienceBaseDescribe:"index__experienceBaseDescribe__rQAtr",drillTag:"index__drillTag__fAFBO",submit:"index__submit__Mxz1b",errorMessage:"index__errorMessage__+7buf"};const E=s},68149:(S,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>E});var i=e(1892),A=e.n(i),u=e(26086),_={};_.insert="head",_.singleton=!1;var s=A()(u.Z,_);const E=u.Z.locals||{}},17268:(S,m,e)=>{"use strict";e.r(m),e.d(m,{default:()=>E});var i=e(1892),A=e.n(i),u=e(424),_={};_.insert="head",_.singleton=!1;var s=A()(u.Z,_);const E=u.Z.locals||{}}}]);

//# sourceMappingURL=564.bundle.js.map