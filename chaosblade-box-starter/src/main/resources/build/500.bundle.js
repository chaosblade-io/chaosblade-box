(self.webpackChunk=self.webpackChunk||[]).push([[500],{70343:function(C,d,e){var o,s,A,r=e(67394);(function(l,t){if(!0)!(s=[d,e(14798)],o=t,A=typeof o=="function"?o.apply(d,s):o,A!==void 0&&(C.exports=A));else var R})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(l,t){"use strict";var R=e(67971);r(l,"__esModule",{value:!0}),l.SearchOptions=l.SearchOptDict=l.ExperimentConstants=void 0,t=R(t);var _={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:t.default.t("Abnormal"),FAILED:t.default.t("Not as expected"),STOPPED:t.default.t("Interrupt"),SUCCESS:t.default.t("Success"),EXCEPTION:t.default.t("Abnormal"),TOTAL:t.default.t("Number of drills")};l.ExperimentConstants=_;var n={1:{name:t.default.t("Success"),status:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_SUCCESS]},2:{name:t.default.t("In progress"),status:_.EXPERIMENT_TASK_STATE_RUNNING,results:[]},3:{name:t.default.t("Interrupt"),value:"3",status:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_REJECTED,_.EXPERIMENT_TASK_RESULT_STOPPED]},4:{name:t.default.t("Not as expected"),value:"4",status:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_FAILED]},5:{name:t.default.t("Abnormal"),status:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_ERROR]}};l.SearchOptDict=n;var f=[{label:t.default.t("All")},{label:t.default.t("Success"),state:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_SUCCESS]},{label:t.default.t("In progress"),state:_.EXPERIMENT_TASK_STATE_RUNNING,results:[]},{label:t.default.t("Interrupt"),state:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_REJECTED,_.EXPERIMENT_TASK_RESULT_STOPPED]},{label:t.default.t("Not as expected"),state:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_FAILED]},{label:t.default.t("Abnormal"),state:_.EXPERIMENT_TASK_STATE_FINISHED,results:[_.EXPERIMENT_TASK_RESULT_ERROR]}];l.SearchOptions=f})},94518:function(C,d,e){var o,s,A,r=e(24596),l=e(67394),t=e(93168),R=e(23587);(function(_,n){if(!0)!(s=[d,e(93484),e(17225),e(77809),e(81853),e(42499),e(63835),e(27378),e(66697),e(98784),e(14798),e(68055),e(61320),e(47696),e(70343),e(99328),e(14870),e(42058)],o=n,A=typeof o=="function"?o.apply(d,s):o,A!==void 0&&(C.exports=A));else var f})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(_,n,f,S,I,i,D,a,O,U,m,h,x,E,g,j,G,W){"use strict";var c=e(67971);l(_,"__esModule",{value:!0}),_.default=J,n=c(n),f=c(f),S=c(S),I=c(I),i=c(i),D=c(D),a=Q(a),O=c(O),U=c(U),m=c(m),h=c(h),x=c(x),E=c(E);function H(u){if(typeof t!="function")return null;var L=new t,B=new t;return(H=function(K){return K?B:L})(u)}function Q(u,L){if(!L&&u&&u.__esModule)return u;if(u===null||r(u)!=="object"&&typeof u!="function")return{default:u};var B=H(L);if(B&&B.has(u))return B.get(u);var N={},K=l&&R;for(var v in u)if(v!=="default"&&Object.prototype.hasOwnProperty.call(u,v)){var F=K?R(u,v):null;F&&(F.get||F.set)?l(N,v,F):N[v]=u[v]}return N.default=u,B&&B.set(u,N),N}var Y=i.default.Column;function J(u){var L=(0,G.useDispatch)(),B=(0,W.useHistory)(),N=(0,a.useState)([]),K=(0,I.default)(N,2),v=K[0],F=K[1],$=(0,a.useState)(1),z=(0,I.default)($,2),w=z[0],k=z[1],q=(0,a.useState)(0),b=(0,I.default)(q,2),ee=b[0],te=b[1];(0,a.useEffect)(function(){var P=u.experimentId;(0,S.default)(regeneratorRuntime.mark(function T(){return regeneratorRuntime.wrap(function(X){for(;;)switch(X.prev=X.next){case 0:return X.next=2,L.experimentDetail.getExperimentTaskPageable({experimentId:P,page:w,size:10},function(p){p&&p.content&&(F(p&&p.content),te(p&&p.total))});case 2:case"end":return X.stop()}},T)}))()},[w]);function ne(P){P&&k(P)}var V=function(T){return U.default.isString(T)&&!U.default.isEmpty(T)||U.default.isNumber(T)&&T?(0,x.default)(T).format("YYYY-MM-DD HH:mm:ss"):T},ae=function(T,Z,X){var p=X.result,y,M="";return T===g.ExperimentConstants.EXPERIMENT_TASK_STATE_STOPPING?(y=a.default.createElement(f.default,{type:"loading",size:"small",style:{marginRight:5}}),M=m.default.t("Stopping")):T===g.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?(y=a.default.createElement(f.default,{type:"loading",size:"small",style:{marginRight:5}}),M=m.default.t("In execution")):T===g.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(p===g.ExperimentConstants.EXPERIMENT_TASK_RESULT_SUCCESS&&(y=a.default.createElement(f.default,{type:"select",style:{color:"#1E8E3E",marginRight:8},size:"xs"}),M=m.default.t("Success")),p===g.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED&&(y=a.default.createElement(D.default,{className:E.default.icon,type:"icon-yichang"}),M=m.default.t("Not as expected")),p===g.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR&&(y=a.default.createElement(D.default,{className:E.default.icon,type:"icon-yichang"}),M=m.default.t("Abnormal")),(p===g.ExperimentConstants.EXPERIMENT_TASK_RESULT_STOPPED||p===g.ExperimentConstants.EXPERIMENT_TASK_RESULT_REJECTED)&&(y=a.default.createElement(D.default,{className:E.default.icon,type:"icon-zhongduan"}),M=m.default.t("Interrupt"))),a.default.createElement("div",{className:E.default.status},y,a.default.createElement("span",null,M))};function le(P){P&&(0,j.pushUrl)(B,"/chaos/experiment/task",{id:P})}var Ee=function(T){return a.default.createElement("div",{className:E.default.optGroup},a.default.createElement("a",{className:E.default.opt,target:"_blank",rel:"noopener noreferrer",onClick:function(){return le(T)}},a.default.createElement(O.default,null,"View details")))};return a.default.createElement("div",null,a.default.createElement(i.default,{className:E.default.body,dataSource:v,isZebra:!0,primaryKey:"taskId",hasBorder:!1,emptyContent:m.default.t("There is no execution record for the drill").toString(),locale:(0,h.default)().Table},a.default.createElement(Y,{title:m.default.t("Exercise name").toString(),dataIndex:"experimentName",width:"25%"}),a.default.createElement(Y,{title:m.default.t("Start time").toString(),dataIndex:"startTime",cell:V,width:"15%"}),a.default.createElement(Y,{title:m.default.t("End Time").toString(),dataIndex:"endTime",cell:V,width:"15%"}),a.default.createElement(Y,{title:m.default.t("State").toString(),dataIndex:"state",cell:ae,width:"10%"}),a.default.createElement(Y,{title:m.default.t("Operation").toString(),dataIndex:"taskId",cell:Ee,width:"15%"})),a.default.createElement("div",{className:E.default.pagination},a.default.createElement(n.default,{current:w,total:ee,pageSize:10,onChange:ne,hideOnlyOnePage:!0,locale:(0,h.default)().Pagination})))}})},48492:function(C,d,e){var o,s,A,r=e(67394);(function(l,t){if(!0)!(s=[d,e(36939),e(17225),e(92243),e(27378),e(66697),e(98784),e(74590),e(55753),e(17640)],o=t,A=typeof o=="function"?o.apply(d,s):o,A!==void 0&&(C.exports=A));else var R})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(l,t,R,_,n,f,S,I,i,D){"use strict";var a=e(67971);r(l,"__esModule",{value:!0}),l.default=void 0,t=a(t),R=a(R),_=a(_),n=a(n),f=a(f),S=a(S),I=a(I),i=a(i);var O=_.default.Tooltip;function U(h){var x,E=h.baseInfo,g=h.permission,j=0,G=E&&((x=E.name)===null||x===void 0?void 0:x.replace(/[^\x00-\xff]/g,"01").length);if(E&&E.description){var W;j=(W=E.description)===null||W===void 0?void 0:W.replace(/[^\x00-\xff]/g,"01").length}return n.default.createElement("div",null,n.default.createElement("div",{className:i.default.item},n.default.createElement("div",{className:i.default.infomation},n.default.createElement(f.default,null,"Basic Information")),(0,D.handleIsAdmin)(g,2)&&n.default.createElement("a",{className:i.default.editInfo,onClick:h.onEditExperimentBaseInfo},n.default.createElement("span",null,n.default.createElement(R.default,{type:"edit",className:i.default.Icon})),n.default.createElement(f.default,null,"Edit basic information"))),n.default.createElement("div",{className:i.default.infoContent},n.default.createElement("div",{className:i.default.infoList},n.default.createElement("div",{className:i.default.item},n.default.createElement("div",{className:i.default.label},n.default.createElement(f.default,null,"Drill name")),G>49?n.default.createElement(O,{trigger:n.default.createElement("div",{className:i.default.nameLong},E&&E.name),align:"b",className:i.default.value},E&&E.name):n.default.createElement("div",{className:i.default.value},E&&E.name)),n.default.createElement("div",{className:i.default.item},n.default.createElement("div",{className:i.default.label},n.default.createElement(f.default,null,"Walkthrough Description")),j>98?n.default.createElement(O,{trigger:n.default.createElement("div",{className:i.default.valueLong},E&&E.description),align:"b",className:i.default.value},E&&E.description):n.default.createElement("div",{className:i.default.value},E&&E.description)),n.default.createElement("div",{className:i.default.item},n.default.createElement("div",{className:i.default.label},n.default.createElement(f.default,null,"Create Time")),n.default.createElement("div",{className:i.default.value},(0,I.default)(E&&E.gmtCreate))),n.default.createElement("div",{className:i.default.item},n.default.createElement("div",{className:i.default.label},n.default.createElement(f.default,null,"Label")),n.default.createElement("div",{className:i.default.value},E&&S.default.map(S.default.defaultTo(E.tags,[]),function(c,H){return n.default.createElement(t.default,{key:"experiment-tag-".concat(H),className:i.default.tag,title:c,size:"medium"},c)}))))))}var m=U;l.default=m})},74590:function(C,d,e){var o,s,A,r=e(67394);(function(l,t){if(!0)!(s=[d,e(61320)],o=t,A=typeof o=="function"?o.apply(d,s):o,A!==void 0&&(C.exports=A));else var R})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(l,t){"use strict";var R=e(67971);r(l,"__esModule",{value:!0}),l.default=void 0,t=R(t);var _=function(S){return S?(0,t.default)(S).format("YYYY-MM-DD HH:mm:ss"):""},n=_;l.default=n})},16588:(C,d,e)=>{"use strict";e.d(d,{Z:()=>t});var o=e(60994),s=e.n(o),A=e(93476),r=e.n(A),l=r()(s());l.push([C.id,`.index__body__vkKgs {
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
`],sourceRoot:""}]),l.locals={body:"index__body__vkKgs",status:"index__status__L95GM",playing:"index__playing__FCaQy",finished:"index__finished__rHV6x",unexpected:"index__unexpected__ZyVWK",error:"index__error__49bWv",optGroup:"index__optGroup__Ce6Fz",opt:"index__opt__h5OSb",pagination:"index__pagination__J4uXg",icon:"index__icon__Q0CXO"};const t=l},40960:(C,d,e)=>{"use strict";e.d(d,{Z:()=>t});var o=e(60994),s=e.n(o),A=e(93476),r=e.n(A),l=r()(s());l.push([C.id,`.index__item__t3ESr {
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
}`],sourceRoot:""}]),l.locals={item:"index__item__t3ESr",label:"index__label__RwFLG",infomation:"index__infomation__KBEn0",editInfo:"index__editInfo__hrF5E",Icon:"index__Icon__DVWY-",nameLong:"index__nameLong__cGgq8",valueLong:"index__valueLong__31eax",value:"index__value__D9Ae2",tag:"index__tag__k9IQL",infoContent:"index__infoContent__ieBn2",infoList:"index__infoList__OtMZa",Divider:"index__Divider__YYAAv"};const t=l},47696:(C,d,e)=>{"use strict";e.r(d),e.d(d,{default:()=>t});var o=e(1892),s=e.n(o),A=e(16588),r={};r.insert="head",r.singleton=!1;var l=s()(A.Z,r);const t=A.Z.locals||{}},55753:(C,d,e)=>{"use strict";e.r(d),e.d(d,{default:()=>t});var o=e(1892),s=e.n(o),A=e(40960),r={};r.insert="head",r.singleton=!1;var l=s()(A.Z,r);const t=A.Z.locals||{}}}]);

//# sourceMappingURL=500.bundle.js.map