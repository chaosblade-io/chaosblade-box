(self.webpackChunk=self.webpackChunk||[]).push([[163],{4163:function(O,A,e){var _,h,m,c=e(24596),u=e(67394),g=e(93168),N=e(23587),j=e(83452),T=e(95315),S=e(63774),w=e(92937);(function(M,r){if(!0)!(h=[A,e(77809),e(81853),e(57379),e(91714),e(47701),e(87286),e(48492),e(94518),e(27378),e(68250),e(93525),e(98784),e(14798),e(7609),e(96291),e(99328),e(14870),e(42058)],_=r,m=typeof _=="function"?_.apply(A,h):_,m!==void 0&&(O.exports=m));else var D})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(M,r,D,l,W,C,B,Z,Y,o,H,Q,t,f,p,I,y,P,U){"use strict";var x=e(67971);u(M,"__esModule",{value:!0}),M.default=void 0,r=x(r),D=x(D),l=x(l),W=x(W),C=x(C),B=x(B),Z=x(Z),Y=x(Y),o=ee(o),H=x(H),Q=x(Q),t=x(t),f=x(f),p=x(p);function b(n){if(typeof g!="function")return null;var a=new g,i=new g;return(b=function(F){return F?i:a})(n)}function ee(n,a){if(!a&&n&&n.__esModule)return n;if(n===null||c(n)!=="object"&&typeof n!="function")return{default:n};var i=b(a);if(i&&i.has(n))return i.get(n);var d={},F=u&&N;for(var K in n)if(K!=="default"&&Object.prototype.hasOwnProperty.call(n,K)){var X=F?N(n,K):null;X&&(X.get||X.set)?u(d,K,X):d[K]=n[K]}return d.default=n,i&&i.set(n,d),d}function G(n,a){var i=j(n);if(T){var d=T(n);a&&(d=d.filter(function(F){return N(n,F).enumerable})),i.push.apply(i,d)}return i}function V(n){for(var a=1;a<arguments.length;a++){var i=arguments[a]!=null?arguments[a]:{};a%2?G(Object(i),!0).forEach(function(d){(0,l.default)(n,d,i[d])}):S?w(n,S(i)):G(Object(i)).forEach(function(d){u(n,d,N(i,d))})}return n}var J=C.default.Item,z=W.default.Item,L=function(){var a=(0,P.useDispatch)(),i=(0,U.useHistory)(),d=(0,P.useSelector)(function(E){return V(V({},E.experimentEditor),E.experimentDataSource)}),F=d.experiment,K=d.tags,X=(0,o.useState)(0),re=(0,D.default)(X,2),$=re[0],oe=re[1],_e=(0,o.useState)(null),ie=(0,D.default)(_e,2),le=ie[0],me=ie[1],pe=(0,o.useState)(!1),de=(0,D.default)(pe,2),te=de[0],se=de[1],k=(0,y.parseQuery)();(0,o.useEffect)(function(){if(a.pageHeader.setTitle(f.default.t("Walkthrough configuration").toString()),a.pageHeader.setBreadCrumbItems(I.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace",value:f.default.t("Space management").toString(),path:"/chaos/workspace/list"},{key:"experiment_editor",value:f.default.t("Walkthrough configuration").toString(),path:"/chaos/experiment/edtior"}])),!t.default.isEmpty(k)){a.experimentEditor.setClearExperiment();var E=k.id,q=k.expertiseId,ne=k.code;t.default.isEmpty(E)?q?(a.experimentEditor.setClearExperiment(),(0,r.default)(regeneratorRuntime.mark(function v(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentByExpertise({expertise_id:q});case 2:case"end":return s.stop()}},v)}))()):ne&&(a.experimentEditor.setClearExperiment(),(0,r.default)(regeneratorRuntime.mark(function v(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentByAppCode({appCode:ne});case 2:case"end":return s.stop()}},v)}))()):(me(E),se(!0),(0,r.default)(regeneratorRuntime.mark(function v(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperimentBaseInfo({experimentId:E});case 2:case"end":return s.stop()}},v)}))(),(0,r.default)(regeneratorRuntime.mark(function v(){return regeneratorRuntime.wrap(function(s){for(;;)switch(s.prev=s.next){case 0:return s.next=2,a.experimentEditor.getExperiment({experimentId:E},function(Be){var ve=Be.flowInfo;t.default.isEmpty(ve)||se(!0)});case 2:case"end":return s.stop()}},v)}))())}},[]);function Ee(){return o.default.createElement(Z.default,{baseInfo:t.default.get(F,"baseInfo",{}),onEditExperimentBaseInfo:Ae})}function Ae(){(0,y.pushUrl)(i,"/chaos/baseInfo/editor",{pass:"detail"})}function ce(E){a.experimentEditor.setUpdateBaseInfo(E)}function ge(E){(0,r.default)(regeneratorRuntime.mark(function q(){return regeneratorRuntime.wrap(function(v){for(;;)switch(v.prev=v.next){case 0:return v.next=2,a.experimentDataSource.getTags(E);case 2:case"end":return v.stop()}},q)}))()}function xe(){ge({key:"",type:0})}function ue(){var E=$+1;oe(E>2?2:E)}function he(){var E=$-1;oe(E<0?0:E)}function fe(){a.experimentEditor.setClearExperiment(),(0,y.pushUrl)(i,"/chaos/experiment/detail")}function Ce(){if($===0)return o.default.createElement(H.default,{isEdit:te,onNext:ue,onBack:fe,isExpertise:!1});if($===1)return o.default.createElement(Q.default,{experimentId:le,isEdit:te,onNext:ue,onPrev:he,onBack:fe,isExpertise:!1})}return o.default.createElement("div",{className:p.default.experimentEditor},te?Ee():o.default.createElement(B.default,{onUpdateBasinfo:ce,tags:K,data:t.default.get(F,"baseInfo",{}),disabled:$===2,onFocusTags:xe}),o.default.createElement(C.default,{className:p.default.tabs,shape:"wrapped"},o.default.createElement(J,{title:f.default.t("Configure").toString()},o.default.createElement("div",{className:p.default.configureItem},o.default.createElement("div",null,o.default.createElement(W.default,{current:$,shape:"circle",labelPlacement:"hoz",className:p.default.steps},o.default.createElement(z,{title:f.default.t("Drill object").toString(),content:f.default.t("Applications and failures").toString()}),o.default.createElement(z,{title:f.default.t("Global configuration").toString(),content:f.default.t("Global parameter settings").toString()})),Ce()))),o.default.createElement(J,{title:f.default.t("Record").toString()},o.default.createElement(Y.default,{experimentId:le}))))},R=L;M.default=R})},87286:function(O,A,e){var _,h,m,c=e(24596),u=e(67394),g=e(93168),N=e(23587);(function(j,T){if(!0)!(h=[A,e(28757),e(8583),e(15286),e(81853),e(27378),e(98784),e(14798),e(68055),e(17268)],_=T,m=typeof _=="function"?_.apply(A,h):_,m!==void 0&&(O.exports=m));else var S})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(j,T,S,w,M,r,D,l,W,C){"use strict";var B=e(67971);u(j,"__esModule",{value:!0}),j.default=void 0,T=B(T),S=B(S),w=B(w),M=B(M),r=Y(r),D=B(D),l=B(l),W=B(W),C=B(C);function Z(t){if(typeof g!="function")return null;var f=new g,p=new g;return(Z=function(y){return y?p:f})(t)}function Y(t,f){if(!f&&t&&t.__esModule)return t;if(t===null||c(t)!=="object"&&typeof t!="function")return{default:t};var p=Z(f);if(p&&p.has(t))return p.get(t);var I={},y=u&&N;for(var P in t)if(P!=="default"&&Object.prototype.hasOwnProperty.call(t,P)){var U=y?N(t,P):null;U&&(U.get||U.set)?u(I,P,U):I[P]=t[P]}return I.default=t,p&&p.set(t,I),I}var o={labelCol:{span:2},wrapperCol:{span:6}};function H(t){var f=(0,r.useState)(!1),p=(0,M.default)(f,2),I=p[0],y=p[1],P=(0,r.useState)(!1),U=(0,M.default)(P,2),x=U[0],b=U[1];function ee(R){z("name",R)}function G(R){z("description",R)}function V(R){if(!D.default.isEmpty(R)&&R.length>5){y(!0);return}z("tags",R),y(!1),b(!1)}function J(){return I?l.default.t("Fill up to 5"):x?l.default.t("1 label can contain up to 30 characters"):""}function z(R,n){var a=t.data,i=t.onUpdateBasinfo;a[R]=n,i(a)}var L=t.data;return r.default.createElement("div",{className:C.default.warp},r.default.createElement(S.default,o,r.default.createElement(S.default.Item,{label:l.default.t("Drill name").toString(),required:!0,requiredTrigger:"onBlur"},r.default.createElement(w.default,{value:L&&L.name||"",className:C.default.experienceBase,maxLength:50,showLimitHint:!0,placeholder:l.default.t("Please enter a drill name").toString(),name:l.default.t("Drill name"),onChange:ee,disabled:t.disabled,"data-autolog":"text=".concat(l.default.t("Please enter a drill name").toString())})),r.default.createElement(S.default.Item,{label:l.default.t("Walkthrough Description").toString()},r.default.createElement(w.default.TextArea,{value:L&&L.description,className:C.default.experienceBaseDescribe,maxLength:1e3,showLimitHint:!0,placeholder:l.default.t("Please enter a walkthrough description").toString(),onChange:G,disabled:t.disabled,"data-autolog":"text=".concat(l.default.t("Please enter a walkthrough description").toString())})),r.default.createElement(S.default.Item,{label:l.default.t("Walkthrough Tags").toString()},r.default.createElement(T.default,{value:L&&L.tags,className:C.default.drillTag,onChange:V,showSearch:!0,dataSource:t.tags,mode:"tag",disabled:t.disabled,onFocus:t.onFocusTags,placeholder:l.default.t("Please enter a walkthrough tag").toString(),notFoundContent:l.default.t("After entering manually, click Enter to add").toString(),"data-autolog":"text=".concat(l.default.t("Enter a walkthrough tag")),locale:(0,W.default)().Select}),J()&&r.default.createElement("div",{className:C.default.errorMessage},J()))))}var Q=H;j.default=Q})},82239:(O,A,e)=>{"use strict";e.d(A,{Z:()=>g});var _=e(60994),h=e.n(_),m=e(93476),c=e.n(m),u=c()(h());u.push([O.id,`.index__experimentEditor__eFdlm {
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
`],sourceRoot:""}]),u.locals={experimentEditor:"index__experimentEditor__eFdlm",tabs:"index__tabs__x28i0",configureItem:"index__configureItem__dZd7I",noConfigContainer:"index__noConfigContainer__Y5f7-",noConfigBox:"index__noConfigBox__TwKXb",customCreate:"index__customCreate__5azRq",noConfig:"index__noConfig__08sM5",noConfigTips:"index__noConfigTips__j9aTu",steps:"index__steps__op-Af"};const g=u},424:(O,A,e)=>{"use strict";e.d(A,{Z:()=>g});var _=e(60994),h=e.n(_),m=e(93476),c=e.n(m),u=c()(h());u.push([O.id,`.index__warp__h61oO {
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
`],sourceRoot:""}]),u.locals={warp:"index__warp__h61oO",experienceBase:"index__experienceBase__JMFre",experienceBaseDescribe:"index__experienceBaseDescribe__rQAtr",drillTag:"index__drillTag__fAFBO",submit:"index__submit__Mxz1b",errorMessage:"index__errorMessage__+7buf"};const g=u},7609:(O,A,e)=>{"use strict";e.r(A),e.d(A,{default:()=>g});var _=e(1892),h=e.n(_),m=e(82239),c={};c.insert="head",c.singleton=!1;var u=h()(m.Z,c);const g=m.Z.locals||{}},17268:(O,A,e)=>{"use strict";e.r(A),e.d(A,{default:()=>g});var _=e(1892),h=e.n(_),m=e(424),c={};c.insert="head",c.singleton=!1;var u=h()(m.Z,c);const g=m.Z.locals||{}}}]);

//# sourceMappingURL=163.bundle.js.map