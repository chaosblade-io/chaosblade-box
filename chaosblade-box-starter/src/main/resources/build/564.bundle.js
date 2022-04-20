(self.webpackChunk=self.webpackChunk||[]).push([[564],{8564:function(R,_,e){var i,m,l,f=e(24596),s=e(67394),E=e(93168),L=e(23587),N=e(83452),I=e(95315),C=e(63774),w=e(92937);(function(O,o){if(!0)!(m=[_,e(72153),e(17534),e(57379),e(77809),e(81853),e(87286),e(27378),e(98784),e(68149),e(96291),e(99328),e(14870),e(42058)],i=o,l=typeof i=="function"?i.apply(_,m):i,l!==void 0&&(R.exports=l));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(O,o,T,g,h,j,X,A,K,U,n,x,c,P){"use strict";var p=e(67971);s(O,"__esModule",{value:!0}),O.default=void 0,o=p(o),T=p(T),g=p(g),h=p(h),j=p(j),X=p(X),A=y(A),K=p(K),U=p(U);function B(t){if(typeof E!="function")return null;var u=new E,r=new E;return(B=function(d){return d?r:u})(t)}function y(t,u){if(!u&&t&&t.__esModule)return t;if(t===null||f(t)!=="object"&&typeof t!="function")return{default:t};var r=B(u);if(r&&r.has(t))return r.get(t);var a={},d=s&&L;for(var v in t)if(v!=="default"&&Object.prototype.hasOwnProperty.call(t,v)){var M=d?L(t,v):null;M&&(M.get||M.set)?s(a,v,M):a[v]=t[v]}return a.default=t,r&&r.set(t,a),a}function z(t,u){var r=N(t);if(I){var a=I(t);u&&(a=a.filter(function(d){return L(t,d).enumerable})),r.push.apply(r,a)}return r}function $(t){for(var u=1;u<arguments.length;u++){var r=arguments[u]!=null?arguments[u]:{};u%2?z(Object(r),!0).forEach(function(a){(0,g.default)(t,a,r[a])}):C?w(t,C(r)):z(Object(r)).forEach(function(a){s(t,a,L(r,a))})}return t}function Z(){var t=(0,c.useDispatch)(),u=(0,P.useHistory)(),r=(0,c.useSelector)(function(D){var F=D.experimentDetail;return F.baseInfo}),a=(0,c.useSelector)(function(D){var F=D.experimentDataSource;return F.tags}),d=(0,A.useState)(null),v=(0,j.default)(d,2),M=v[0],Y=v[1];(0,A.useEffect)(function(){var D=(0,x.parseQuery)(),F=K.default.get(D,"id","");F&&(0,h.default)(regeneratorRuntime.mark(function W(){return regeneratorRuntime.wrap(function(S){for(;;)switch(S.prev=S.next){case 0:return S.next=2,t.experimentDetail.getExperimentBaseInfo({experimentId:F},function(J){return Y(function(){var Q=J.workspaces,te=Q===void 0?[]:Q,V=[];return K.default.forEach(te,function(ne){return V.push(ne.workspaceId)}),$($({},J),{},{workspaces:V})})});case 2:case"end":return S.stop()}},W)}))()},[]),(0,A.useEffect)(function(){t.pageHeader.setTitle("\u6F14\u7EC3\u57FA\u672C\u4FE1\u606F"),t.pageHeader.setBreadCrumbItems(n.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace",value:"\u7A7A\u95F4\u7BA1\u7406",path:"/chaos/workspace/list"},{key:"experiment_detail",value:"\u6F14\u7EC3\u8BE6\u60C5",path:"/chaos/experiment/detail"},{key:"experiment_baseInfo",value:"\u6F14\u7EC3\u57FA\u672C\u4FE1\u606F",path:"/chaos/baseInfo/editor"}]))});function G(D){t.experimentDetail.setUpdateBaseInfo(D)}function b(){var D=(0,x.parseQuery)(),F=K.default.get(D,"id","");(0,h.default)(regeneratorRuntime.mark(function W(){return regeneratorRuntime.wrap(function(S){for(;;)switch(S.prev=S.next){case 0:return S.next=2,t.experimentDetail.updateExperimentBasicInfo($($({},r),{},{experimentId:F}),function(J){J&&T.default.success("\u66F4\u65B0\u6210\u529F\uFF01"),(0,x.pushUrl)(u,"/chaos/experiment/detail")});case 2:case"end":return S.stop()}},W)}))()}function k(){t.experimentEditor.setClearExperiment(),(0,x.pushUrl)(u,"/chaos/experiment/detail")}function q(){(0,h.default)(regeneratorRuntime.mark(function D(){return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:return W.next=2,t.experimentDataSource.getTags({key:"",type:0});case 2:case"end":return W.stop()}},D)}))()}return A.default.createElement("div",{className:U.default.container},A.default.createElement(X.default,{data:M,tags:a,disabled:!1,onUpdateBasinfo:G,onFocusTags:q}),A.default.createElement("div",{className:U.default.Divider}),A.default.createElement("div",null,A.default.createElement(o.default,{className:U.default.headerButton,type:"primary",onClick:b},"\u786E\u5B9A"),A.default.createElement(o.default,{className:U.default.headerButton,onClick:k},"\u53D6\u6D88\u5E76\u8FD4\u56DE\u8BE6\u60C5")))}var H=Z;O.default=H})},87286:function(R,_,e){var i,m,l,f=e(24596),s=e(67394),E=e(93168),L=e(23587);(function(N,I){if(!0)!(m=[_,e(28757),e(8583),e(15286),e(81853),e(27378),e(98784),e(17268)],i=I,l=typeof i=="function"?i.apply(_,m):i,l!==void 0&&(R.exports=l));else var C})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(N,I,C,w,O,o,T,g){"use strict";var h=e(67971);s(N,"__esModule",{value:!0}),N.default=void 0,I=h(I),C=h(C),w=h(w),O=h(O),o=X(o),T=h(T),g=h(g);function j(n){if(typeof E!="function")return null;var x=new E,c=new E;return(j=function(p){return p?c:x})(n)}function X(n,x){if(!x&&n&&n.__esModule)return n;if(n===null||f(n)!=="object"&&typeof n!="function")return{default:n};var c=j(x);if(c&&c.has(n))return c.get(n);var P={},p=s&&L;for(var B in n)if(B!=="default"&&Object.prototype.hasOwnProperty.call(n,B)){var y=p?L(n,B):null;y&&(y.get||y.set)?s(P,B,y):P[B]=n[B]}return P.default=n,c&&c.set(n,P),P}var A={labelCol:{span:2},wrapperCol:{span:6}};function K(n){var x=(0,o.useState)(!1),c=(0,O.default)(x,2),P=c[0],p=c[1],B=(0,o.useState)(!1),y=(0,O.default)(B,2),z=y[0],$=y[1];function Z(d){r("name",d)}function H(d){r("description",d)}function t(d){if(!T.default.isEmpty(d)&&d.length>5){p(!0);return}r("tags",d),p(!1),$(!1)}function u(){return P?"\u6700\u591A\u586B\u51995\u4E2A\uFF01":z?"1\u4E2A\u6807\u7B7E\u6700\u591A\u5305\u542B30\u4E2A\u5B57\u7B26\uFF01":""}function r(d,v){var M=n.data,Y=n.onUpdateBasinfo;M[d]=v,Y(M)}var a=n.data;return o.default.createElement("div",{className:g.default.warp},o.default.createElement(C.default,A,o.default.createElement(C.default.Item,{label:"\u6F14\u7EC3\u540D\u79F0",required:!0,requiredTrigger:"onBlur"},o.default.createElement(w.default,{value:a&&a.name||"",className:g.default.experienceBase,maxLength:50,showLimitHint:!0,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u540D\u79F0",name:"\u6F14\u7EC3\u540D\u79F0",onChange:Z,disabled:n.disabled,"data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u540D\u79F0"})),o.default.createElement(C.default.Item,{label:"\u6F14\u7EC3\u63CF\u8FF0"},o.default.createElement(w.default.TextArea,{value:a&&a.description,className:g.default.experienceBaseDescribe,maxLength:1e3,showLimitHint:!0,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u63CF\u8FF0\u4FE1\u606F",onChange:H,disabled:n.disabled,"data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u63CF\u8FF0"})),o.default.createElement(C.default.Item,{label:"\u6F14\u7EC3\u6807\u7B7E"},o.default.createElement(I.default,{value:a&&a.tags,className:g.default.drillTag,onChange:t,showSearch:!0,dataSource:n.tags,mode:"tag",disabled:n.disabled,onFocus:n.onFocusTags,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u6807\u7B7E",notFoundContent:"\u624B\u52A8\u8F93\u5165\u540E\u70B9\u51FB\u56DE\u8F66\u6DFB\u52A0","data-autolog":"text=\u8F93\u5165\u6F14\u7EC3\u6807\u7B7E"}),u()&&o.default.createElement("div",{className:g.default.errorMessage},u()))))}var U=K;N.default=U})},26086:(R,_,e)=>{"use strict";e.d(_,{Z:()=>E});var i=e(60994),m=e.n(i),l=e(93476),f=e.n(l),s=f()(m());s.push([R.id,`.index__container__EJi4F {
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
`],sourceRoot:""}]),s.locals={container:"index__container__EJi4F",headerButton:"index__headerButton__8+VJw",Divider:"index__Divider__JcOS5"};const E=s},424:(R,_,e)=>{"use strict";e.d(_,{Z:()=>E});var i=e(60994),m=e.n(i),l=e(93476),f=e.n(l),s=f()(m());s.push([R.id,`.index__warp__h61oO {
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
`],sourceRoot:""}]),s.locals={warp:"index__warp__h61oO",experienceBase:"index__experienceBase__JMFre",experienceBaseDescribe:"index__experienceBaseDescribe__rQAtr",drillTag:"index__drillTag__fAFBO",submit:"index__submit__Mxz1b",errorMessage:"index__errorMessage__+7buf"};const E=s},68149:(R,_,e)=>{"use strict";e.r(_),e.d(_,{default:()=>E});var i=e(1892),m=e.n(i),l=e(26086),f={};f.insert="head",f.singleton=!1;var s=m()(l.Z,f);const E=l.Z.locals||{}},17268:(R,_,e)=>{"use strict";e.r(_),e.d(_,{default:()=>E});var i=e(1892),m=e.n(i),l=e(424),f={};f.insert="head",f.singleton=!1;var s=m()(l.Z,f);const E=l.Z.locals||{}}}]);

//# sourceMappingURL=564.bundle.js.map