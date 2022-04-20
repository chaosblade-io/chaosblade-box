(self.webpackChunk=self.webpackChunk||[]).push([[500],{70343:function(f,o,e){var l,s,i,d=e(67394);(function(a,n){if(!0)!(s=[o],l=n,i=typeof l=="function"?l.apply(o,s):l,i!==void 0&&(f.exports=i));else var m})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(a){"use strict";d(a,"__esModule",{value:!0}),a.SearchOptions=a.SearchOptDict=a.ExperimentConstants=void 0;var n={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:"\u5F02\u5E38",FAILED:"\u4E0D\u7B26\u5408\u9884\u671F",STOPPED:"\u4E2D\u65AD",SUCCESS:"\u6210\u529F",EXCEPTION:"\u5F02\u5E38",TOTAL:"\u6F14\u7EC3\u6B21\u6570"};a.ExperimentConstants=n;var m={1:{name:"\u6210\u529F",status:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_SUCCESS]},2:{name:"\u8FDB\u884C\u4E2D",status:n.EXPERIMENT_TASK_STATE_RUNNING,results:[]},3:{name:"\u4E2D\u65AD",value:"3",status:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_REJECTED,n.EXPERIMENT_TASK_RESULT_STOPPED]},4:{name:"\u4E0D\u7B26\u5408\u9884\u671F",value:"4",status:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_FAILED]},5:{name:"\u5F02\u5E38",status:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_ERROR]}};a.SearchOptDict=m;var p=[{label:"\u5168\u90E8"},{label:"\u6210\u529F",state:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_SUCCESS]},{label:"\u8FDB\u884C\u4E2D",state:n.EXPERIMENT_TASK_STATE_RUNNING,results:[]},{label:"\u4E2D\u65AD",state:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_REJECTED,n.EXPERIMENT_TASK_RESULT_STOPPED]},{label:"\u4E0D\u7B26\u5408\u9884\u671F",state:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_FAILED]},{label:"\u5F02\u5E38",state:n.EXPERIMENT_TASK_STATE_FINISHED,results:[n.EXPERIMENT_TASK_RESULT_ERROR]}];a.SearchOptions=p})},94518:function(f,o,e){var l,s,i,d=e(24596),a=e(67394),n=e(93168),m=e(23587);(function(p,t){if(!0)!(s=[o,e(93484),e(17225),e(77809),e(81853),e(42499),e(27378),e(47696),e(63835),e(98784),e(61320),e(70343),e(99328),e(14870),e(42058)],l=t,i=typeof l=="function"?l.apply(o,s):l,i!==void 0&&(f.exports=i));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(p,t,T,g,_,M,E,C,N,v,I,c,A,j,W){"use strict";var S=e(67971);a(p,"__esModule",{value:!0}),p.default=b,t=S(t),T=S(T),g=S(g),_=S(_),M=S(M),E=Y(E),C=S(C),N=S(N),v=S(v),I=S(I);function F(r){if(typeof n!="function")return null;var O=new n,B=new n;return(F=function(L){return L?B:O})(r)}function Y(r,O){if(!O&&r&&r.__esModule)return r;if(r===null||d(r)!=="object"&&typeof r!="function")return{default:r};var B=F(O);if(B&&B.has(r))return B.get(r);var h={},L=a&&m;for(var x in r)if(x!=="default"&&Object.prototype.hasOwnProperty.call(r,x)){var K=L?m(r,x):null;K&&(K.get||K.set)?a(h,x,K):h[x]=r[x]}return h.default=r,B&&B.set(r,h),h}var D=M.default.Column;function b(r){var O=(0,j.useDispatch)(),B=(0,W.useHistory)(),h=(0,E.useState)([]),L=(0,_.default)(h,2),x=L[0],K=L[1],Z=(0,E.useState)(1),G=(0,_.default)(Z,2),H=G[0],Q=G[1],J=(0,E.useState)(0),w=(0,_.default)(J,2),$=w[0],k=w[1];(0,E.useEffect)(function(){var P=r.experimentId;(0,g.default)(regeneratorRuntime.mark(function u(){return regeneratorRuntime.wrap(function(X){for(;;)switch(X.prev=X.next){case 0:return X.next=2,O.experimentDetail.getExperimentTaskPageable({experimentId:P,page:H,size:10},function(R){R&&R.content&&(K(R&&R.content),k(R&&R.total))});case 2:case"end":return X.stop()}},u)}))()},[H]);function q(P){P&&Q(P)}var z=function(u){return v.default.isString(u)&&!v.default.isEmpty(u)||v.default.isNumber(u)&&u?(0,I.default)(u).format("YYYY-MM-DD HH:mm:ss"):u},ee=function(u,V,X){var R=X.result,y,U="";return u===c.ExperimentConstants.EXPERIMENT_TASK_STATE_STOPPING?(y=E.default.createElement(T.default,{type:"loading",size:"small",style:{marginRight:5}}),U="\u6B63\u5728\u505C\u6B62"):u===c.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?(y=E.default.createElement(T.default,{type:"loading",size:"small",style:{marginRight:5}}),U="\u6267\u884C\u4E2D"):u===c.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(R===c.ExperimentConstants.EXPERIMENT_TASK_RESULT_SUCCESS&&(y=E.default.createElement(T.default,{type:"select",style:{color:"#1E8E3E",marginRight:8},size:"xs"}),U="\u6210\u529F"),R===c.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED&&(y=E.default.createElement(N.default,{className:C.default.icon,type:"icon-yichang"}),U="\u4E0D\u7B26\u5408\u9884\u671F"),R===c.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR&&(y=E.default.createElement(N.default,{className:C.default.icon,type:"icon-yichang"}),U="\u5F02\u5E38"),(R===c.ExperimentConstants.EXPERIMENT_TASK_RESULT_STOPPED||R===c.ExperimentConstants.EXPERIMENT_TASK_RESULT_REJECTED)&&(y=E.default.createElement(N.default,{className:C.default.icon,type:"icon-zhongduan"}),U="\u4E2D\u65AD")),E.default.createElement("div",{className:C.default.status},y,E.default.createElement("span",null,U))};function ne(P){P&&(0,A.pushUrl)(B,"/chaos/experiment/task",{id:P})}var te=function(u){return E.default.createElement("div",{className:C.default.optGroup},E.default.createElement("a",{className:C.default.opt,target:"_blank",rel:"noopener noreferrer",onClick:function(){return ne(u)}},"\u67E5\u770B\u8BE6\u60C5"))};return E.default.createElement("div",null,E.default.createElement(M.default,{className:C.default.body,dataSource:x,isZebra:!0,primaryKey:"taskId",hasBorder:!1,emptyContent:"\u6F14\u7EC3\u65E0\u6267\u884C\u8BB0\u5F55"},E.default.createElement(D,{title:"\u6F14\u7EC3\u540D",dataIndex:"experimentName",width:"25%"}),E.default.createElement(D,{title:"\u5F00\u59CB\u65F6\u95F4",dataIndex:"startTime",cell:z,width:"15%"}),E.default.createElement(D,{title:"\u7ED3\u675F\u65F6\u95F4",dataIndex:"endTime",cell:z,width:"15%"}),E.default.createElement(D,{title:"\u72B6\u6001",dataIndex:"state",cell:ee,width:"10%"}),E.default.createElement(D,{title:"\u64CD\u4F5C",dataIndex:"taskId",cell:te,width:"15%"})),E.default.createElement("div",{className:C.default.pagination},E.default.createElement(t.default,{current:H,total:$,pageSize:10,onChange:q,hideOnlyOnePage:!0})))}})},48492:function(f,o,e){var l,s,i,d=e(67394);(function(a,n){if(!0)!(s=[o,e(36939),e(17225),e(92243),e(27378),e(98784),e(74590),e(55753),e(17640)],l=n,i=typeof l=="function"?l.apply(o,s):l,i!==void 0&&(f.exports=i));else var m})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(a,n,m,p,t,T,g,_,M){"use strict";var E=e(67971);d(a,"__esModule",{value:!0}),a.default=void 0,n=E(n),m=E(m),p=E(p),t=E(t),T=E(T),g=E(g),_=E(_);var C=p.default.Tooltip;function N(I){var c,A=I.baseInfo,j=I.permission,W=0,S=A&&((c=A.name)===null||c===void 0?void 0:c.replace(/[^\x00-\xff]/g,"01").length);if(A&&A.description){var F;W=(F=A.description)===null||F===void 0?void 0:F.replace(/[^\x00-\xff]/g,"01").length}return t.default.createElement("div",null,t.default.createElement("div",{className:_.default.item},t.default.createElement("div",{className:_.default.infomation},"\u57FA\u672C\u4FE1\u606F"),(0,M.handleIsAdmin)(j,2)&&t.default.createElement("a",{className:_.default.editInfo,onClick:I.onEditExperimentBaseInfo},t.default.createElement("span",null,t.default.createElement(m.default,{type:"edit",className:_.default.Icon})),"\u7F16\u8F91\u57FA\u672C\u4FE1\u606F")),t.default.createElement("div",{className:_.default.infoContent},t.default.createElement("div",{className:_.default.infoList},t.default.createElement("div",{className:_.default.item},t.default.createElement("div",{className:_.default.label},"\u6F14\u7EC3\u540D\u79F0"),S>49?t.default.createElement(C,{trigger:t.default.createElement("div",{className:_.default.nameLong},A&&A.name),align:"b",className:_.default.value},A&&A.name):t.default.createElement("div",{className:_.default.value},A&&A.name)),t.default.createElement("div",{className:_.default.item},t.default.createElement("div",{className:_.default.label},"\u6F14\u7EC3\u63CF\u8FF0"),W>98?t.default.createElement(C,{trigger:t.default.createElement("div",{className:_.default.valueLong},A&&A.description),align:"b",className:_.default.value},A&&A.description):t.default.createElement("div",{className:_.default.value},A&&A.description)),t.default.createElement("div",{className:_.default.item},t.default.createElement("div",{className:_.default.label},"\u521B\u5EFA\u65F6\u95F4"),t.default.createElement("div",{className:_.default.value},(0,g.default)(A&&A.gmtCreate))),t.default.createElement("div",{className:_.default.item},t.default.createElement("div",{className:_.default.label},"\u6807\u7B7E"),t.default.createElement("div",{className:_.default.value},A&&T.default.map(T.default.defaultTo(A.tags,[]),function(Y,D){return t.default.createElement(n.default,{key:"experiment-tag-".concat(D),className:_.default.tag,title:Y,size:"medium"},Y)}))))))}var v=N;a.default=v})},74590:function(f,o,e){var l,s,i,d=e(67394);(function(a,n){if(!0)!(s=[o,e(61320)],l=n,i=typeof l=="function"?l.apply(o,s):l,i!==void 0&&(f.exports=i));else var m})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(a,n){"use strict";var m=e(67971);d(a,"__esModule",{value:!0}),a.default=void 0,n=m(n);var p=function(g){return g?(0,n.default)(g).format("YYYY-MM-DD HH:mm:ss"):""},t=p;a.default=t})},16588:(f,o,e)=>{"use strict";e.d(o,{Z:()=>n});var l=e(60994),s=e.n(l),i=e(93476),d=e.n(i),a=d()(s());a.push([f.id,`.index__body__vkKgs {
  margin-top: 16px;
}

.index__status__L95GM {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #373D41;
  margin-left: 10px;
}

.index__status__L95GM.index__playing__FCaQy {
    color: #7EC12D;
  }

.index__status__L95GM.index__finished__rHV6x {
    color: #00C1DE;
  }

.index__status__L95GM.index__unexpected__ZyVWK {
    color: #F5A623;
  }

.index__status__L95GM.index__error__49bWv {
    color: #FF4F4C;
  }

.index__optGroup__Ce6Fz {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: center;
}

.index__opt__h5OSb {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #0066CC;
  letter-spacing: 0;
  cursor: pointer;
  margin: 1px 8px;
}

.index__pagination__J4uXg {
  padding: 9px 0;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}

.index__icon__Q0CXO {
  width: 16px;
  height: 16px;
  margin-right: 5px;
  position: relative;
  top: 2px;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/common/ExperimentTaskHistory/index.css"],names:[],mappings:"AAAA;EACE,gBAAgB;AAClB;;AAEA;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,iBAAiB;AAiBnB;;AAfE;IACE,cAAc;EAChB;;AAEA;IACE,cAAc;EAChB;;AAEA;IACE,cAAc;EAChB;;AAEA;IACE,cAAc;EAChB;;AAGF;EACE,aAAa;EACb,mBAAmB;EACnB,eAAe;EACf,2BAA2B;EAC3B,mBAAmB;AACrB;;AAEA;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,iBAAiB;EACjB,eAAe;EACf,eAAe;AACjB;;AAEA;EACE,cAAc;EACd,aAAa;EACb,mBAAmB;EACnB,yBAAyB;EACzB,mBAAmB;AACrB;;AAEA;EACE,WAAW;EACX,YAAY;EACZ,iBAAiB;EACjB,kBAAkB;EAClB,QAAQ;AACV",sourcesContent:[`.body {
  margin-top: 16px;
}

.status {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #373D41;
  margin-left: 10px;

  &.playing {
    color: #7EC12D;
  }

  &.finished {
    color: #00C1DE;
  }

  &.unexpected {
    color: #F5A623;
  }

  &.error {
    color: #FF4F4C;
  }
}

.optGroup {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: center;
}

.opt {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #0066CC;
  letter-spacing: 0;
  cursor: pointer;
  margin: 1px 8px;
}

.pagination {
  padding: 9px 0;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}

.icon {
  width: 16px;
  height: 16px;
  margin-right: 5px;
  position: relative;
  top: 2px;
}
`],sourceRoot:""}]),a.locals={body:"index__body__vkKgs",status:"index__status__L95GM",playing:"index__playing__FCaQy",finished:"index__finished__rHV6x",unexpected:"index__unexpected__ZyVWK",error:"index__error__49bWv",optGroup:"index__optGroup__Ce6Fz",opt:"index__opt__h5OSb",pagination:"index__pagination__J4uXg",icon:"index__icon__Q0CXO"};const n=a},40960:(f,o,e)=>{"use strict";e.d(o,{Z:()=>n});var l=e(60994),s=e.n(l),i=e(93476),d=e.n(i),a=d()(s());a.push([f.id,`.index__item__t3ESr {
  width: 100%;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 32px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center; 
}

.index__item__t3ESr .index__label__RwFLG {
  min-width: 100px;
  color: #8C8C8C;
  margin-right: 22px;
  text-align: left;
}

.index__infomation__KBEn0 {
  font-size: 14px;
  color: #333;
  margin-right: 62px;
  text-align: left;
}

.index__editInfo__hrF5E {
  text-decoration: none;
  color: #0070CC;
  cursor:pointer;
  line-height: 20px;
}

.index__Icon__DVWY- {
  font-size: 14px;
  color: #0070CC;
  margin-right: 8px;
}

.index__nameLong__cGgq8 {
  width: 50%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.index__valueLong__31eax {
  color: #262626;
  width: 50%;
  word-break: break-all;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.index__value__D9Ae2 {
  width: 50%;
  color: #262626;
}

.index__tag__k9IQL {
  margin-right: 5px;
  max-width: 150px;
}

.index__infoContent__ieBn2 {
  width: 100%;
  display: flex;
  justify-content : flex-start;
}

.index__infoList__OtMZa {
  width: 50%;
}

.index__infoList__OtMZa:last-child {
  display: flex;
  flex-direction: column;
}

.index__Divider__YYAAv {
  width: 100%;
  height: 1px;
  background: #e8e8e8;
  margin: 24px 0;
}`,"",{version:3,sources:["webpack://./pages/Chaos/common/BaseInfoView/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,+BAA+B;EAC/B,eAAe;EACf,iBAAiB;EACjB,aAAa;EACb,mBAAmB;EACnB,2BAA2B;EAC3B,mBAAmB;AACrB;;AAEA;EACE,gBAAgB;EAChB,cAAc;EACd,kBAAkB;EAClB,gBAAgB;AAClB;;AAEA;EACE,eAAe;EACf,WAAW;EACX,kBAAkB;EAClB,gBAAgB;AAClB;;AAEA;EACE,qBAAqB;EACrB,cAAc;EACd,cAAc;EACd,iBAAiB;AACnB;;AAEA;EACE,eAAe;EACf,cAAc;EACd,iBAAiB;AACnB;;AAEA;EACE,UAAU;EACV,gBAAgB;EAChB,mBAAmB;EACnB,uBAAuB;EACvB,eAAe;AACjB;;AAEA;EACE,cAAc;EACd,UAAU;EACV,qBAAqB;EACrB,oBAAoB;EACpB,4BAA4B;EAC5B,qBAAqB;EACrB,gBAAgB;AAClB;;AAEA;EACE,UAAU;EACV,cAAc;AAChB;;AAEA;EACE,iBAAiB;EACjB,gBAAgB;AAClB;;AAEA;EACE,WAAW;EACX,aAAa;EACb,4BAA4B;AAC9B;;AAEA;EACE,UAAU;AACZ;;AAEA;EACE,aAAa;EACb,sBAAsB;AACxB;;AAEA;EACE,WAAW;EACX,WAAW;EACX,mBAAmB;EACnB,cAAc;AAChB",sourcesContent:[`.item {
  width: 100%;
  font-family: PingFangSC-Regular;
  font-size: 12px;
  line-height: 32px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center; 
}

.item .label {
  min-width: 100px;
  color: #8C8C8C;
  margin-right: 22px;
  text-align: left;
}

.infomation {
  font-size: 14px;
  color: #333;
  margin-right: 62px;
  text-align: left;
}

.editInfo {
  text-decoration: none;
  color: #0070CC;
  cursor:pointer;
  line-height: 20px;
}

.Icon {
  font-size: 14px;
  color: #0070CC;
  margin-right: 8px;
}

.nameLong {
  width: 50%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  cursor: pointer;
}

.valueLong {
  color: #262626;
  width: 50%;
  word-break: break-all;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.value {
  width: 50%;
  color: #262626;
}

.tag {
  margin-right: 5px;
  max-width: 150px;
}

.infoContent {
  width: 100%;
  display: flex;
  justify-content : flex-start;
}

.infoList {
  width: 50%;
}

.infoList:last-child {
  display: flex;
  flex-direction: column;
}

.Divider {
  width: 100%;
  height: 1px;
  background: #e8e8e8;
  margin: 24px 0;
}`],sourceRoot:""}]),a.locals={item:"index__item__t3ESr",label:"index__label__RwFLG",infomation:"index__infomation__KBEn0",editInfo:"index__editInfo__hrF5E",Icon:"index__Icon__DVWY-",nameLong:"index__nameLong__cGgq8",valueLong:"index__valueLong__31eax",value:"index__value__D9Ae2",tag:"index__tag__k9IQL",infoContent:"index__infoContent__ieBn2",infoList:"index__infoList__OtMZa",Divider:"index__Divider__YYAAv"};const n=a},47696:(f,o,e)=>{"use strict";e.r(o),e.d(o,{default:()=>n});var l=e(1892),s=e.n(l),i=e(16588),d={};d.insert="head",d.singleton=!1;var a=s()(i.Z,d);const n=i.Z.locals||{}},55753:(f,o,e)=>{"use strict";e.r(o),e.d(o,{default:()=>n});var l=e(1892),s=e.n(l),i=e(40960),d={};d.insert="head",d.singleton=!1;var a=s()(i.Z,d);const n=i.Z.locals||{}}}]);

//# sourceMappingURL=500.bundle.js.map