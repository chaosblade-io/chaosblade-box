(self.webpackChunk=self.webpackChunk||[]).push([[821],{93484:function(w,C,e){"use strict";var d=this&&this.__assign||function(){return d=Object.assign||function(A){for(var S,E=1,p=arguments.length;E<p;E++){S=arguments[E];for(var r in S)Object.prototype.hasOwnProperty.call(S,r)&&(A[r]=S[r])}return A},d.apply(this,arguments)},I=this&&this.__importDefault||function(A){return A&&A.__esModule?A:{default:A}};Object.defineProperty(C,"__esModule",{value:!0});var c=e(30156),y=e(46949),m=I(e(27378)),f=e(67056),T=function(A){var S=f.useCssVar("--alicloudfe-components-theme").trim(),E=function(){return S.startsWith("hybridcloud")||S.startsWith("yunxiao")?"arrow-only":"normal"}();return m.default.createElement(c.Pagination,d({shape:E},A))};C.default=y.withThemeClass(T)},13488:(w,C,e)=>{"use strict";Object.defineProperty(C,"__esModule",{value:!0});var d=e(30156);C.default=d.Progress},94188:function(w,C,e){"use strict";var d=this&&this.__assign||function(){return d=Object.assign||function(r){for(var a,_=1,i=arguments.length;_<i;_++){a=arguments[_];for(var B in a)Object.prototype.hasOwnProperty.call(a,B)&&(r[B]=a[B])}return r},d.apply(this,arguments)},I=this&&this.__createBinding||(Object.create?function(r,a,_,i){i===void 0&&(i=_),Object.defineProperty(r,i,{enumerable:!0,get:function(){return a[_]}})}:function(r,a,_,i){i===void 0&&(i=_),r[i]=a[_]}),c=this&&this.__setModuleDefault||(Object.create?function(r,a){Object.defineProperty(r,"default",{enumerable:!0,value:a})}:function(r,a){r.default=a}),y=this&&this.__importStar||function(r){if(r&&r.__esModule)return r;var a={};if(r!=null)for(var _ in r)_!=="default"&&Object.hasOwnProperty.call(r,_)&&I(a,r,_);return c(a,r),a},m=this&&this.__spreadArrays||function(){for(var r=0,a=0,_=arguments.length;a<_;a++)r+=arguments[a].length;for(var i=Array(r),B=0,a=0;a<_;a++)for(var u=arguments[a],R=0,t=u.length;R<t;R++,B++)i[B]=u[R];return i},f=this&&this.__importDefault||function(r){return r&&r.__esModule?r:{default:r}};Object.defineProperty(C,"__esModule",{value:!0});var T=e(30156),A=y(e(27378)),S=f(e(60042)),E=y(e(1073)),p=A.default.forwardRef(function(r,a){var _=A.useState(!1),i=_[0],B=_[1],u=A.useState(!1),R=u[0],t=u[1],W=A.useCallback(function(h){B(!0),typeof r.onFocus=="function"&&r.onFocus(h)},[r.onFocus]),n=A.useCallback(function(h){B(!1),typeof r.onBlur=="function"&&r.onBlur(h)},[r.onBlur]),g=A.useCallback(function(h){for(var x=[],l=1;l<arguments.length;l++)x[l-1]=arguments[l];t(h),typeof r.onVisibleChange=="function"&&r.onVisibleChange.apply(r,m([h],x))},[r.onVisibleChange]),o=E.useDefaultOffsetY(),s=A.useMemo(function(){var h,x=d({align:"tl bl",offset:[0,o]},(h=r.filterProps)===null||h===void 0?void 0:h.popupProps),l=d(d({},r.filterProps),{popupProps:x});return l},[o,r.filterProps]);return A.default.createElement(T.Search,d({},r,{ref:a,onFocus:W,onBlur:n,onVisibleChange:g,className:S.default(r.className,r.searchText?"custom-search-text":null,i?"focusing":!1,R?"visible":!1,r.disabled?"disabled":!1,r.searchText?null:"next-search-no-custom-search-text"),filterProps:s}))});C.default=E.default(p)},42499:function(w,C,e){"use strict";var d=this&&this.__assign||function(){return d=Object.assign||function(p){for(var r,a=1,_=arguments.length;a<_;a++){r=arguments[a];for(var i in r)Object.prototype.hasOwnProperty.call(r,i)&&(p[i]=r[i])}return p},d.apply(this,arguments)},I=this&&this.__rest||function(p,r){var a={};for(var _ in p)Object.prototype.hasOwnProperty.call(p,_)&&r.indexOf(_)<0&&(a[_]=p[_]);if(p!=null&&typeof Object.getOwnPropertySymbols=="function")for(var i=0,_=Object.getOwnPropertySymbols(p);i<_.length;i++)r.indexOf(_[i])<0&&Object.prototype.propertyIsEnumerable.call(p,_[i])&&(a[_[i]]=p[_[i]]);return a},c=this&&this.__importDefault||function(p){return p&&p.__esModule?p:{default:p}};Object.defineProperty(C,"__esModule",{value:!0});var y=c(e(27378)),m=e(30156),f=c(e(60042)),T=c(e(55839)),A=e(67056),S=function(p){var r,a=p.hasBorder,_=p.rowSelection,i=p.className,B=I(p,["hasBorder","rowSelection","className"]),u=A.useCssVar("--alicloudfe-components-theme"),R=u.trim()==="wind";return a===void 0&&(a=R),y.default.createElement(m.Table,d({hasBorder:a,rowSelection:_,className:f.default(i,(r={},r["with-row-select"]=!!_,r["is-wind"]=R,r))},B))};T.default(S,m.Table);var E=S;C.default=E},32186:(w,C,e)=>{w.exports=e(4275)},70343:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(14798)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f){"use strict";var T=e(67971);y(m,"__esModule",{value:!0}),m.SearchOptions=m.SearchOptDict=m.ExperimentConstants=void 0,f=T(f);var A={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:f.default.t("Abnormal"),FAILED:f.default.t("Not as expected"),STOPPED:f.default.t("Interrupt"),SUCCESS:f.default.t("Success"),EXCEPTION:f.default.t("Abnormal"),TOTAL:f.default.t("Number of drills")};m.ExperimentConstants=A;var S={1:{name:f.default.t("Success"),status:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_SUCCESS]},2:{name:f.default.t("In progress"),status:A.EXPERIMENT_TASK_STATE_RUNNING,results:[]},3:{name:f.default.t("Interrupt"),value:"3",status:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_REJECTED,A.EXPERIMENT_TASK_RESULT_STOPPED]},4:{name:f.default.t("Not as expected"),value:"4",status:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_FAILED]},5:{name:f.default.t("Abnormal"),status:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_ERROR]}};m.SearchOptDict=S;var E=[{label:f.default.t("All")},{label:f.default.t("Success"),state:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_SUCCESS]},{label:f.default.t("In progress"),state:A.EXPERIMENT_TASK_STATE_RUNNING,results:[]},{label:f.default.t("Interrupt"),state:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_REJECTED,A.EXPERIMENT_TASK_RESULT_STOPPED]},{label:f.default.t("Not as expected"),state:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_FAILED]},{label:f.default.t("Abnormal"),state:A.EXPERIMENT_TASK_STATE_FINISHED,results:[A.EXPERIMENT_TASK_RESULT_ERROR]}];m.SearchOptions=E})},78583:function(w,C,e){var d,I,c,y=e(24596),m=e(14176),f=e(67394),T=e(93168),A=e(23587);(function(S,E){if(!0)!(I=[C,e(14176),e(12955),e(9863),e(17225),e(81853),e(27378),e(98784),e(14798),e(61320),e(19002),e(73014)],d=E,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var p})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(S,E,p,r,a,_,i,B,u,R,t,W){"use strict";var n=e(67971);f(S,"__esModule",{value:!0}),S.default=s,E=n(E),p=n(p),r=n(r),a=n(a),_=n(_),i=o(i),B=n(B),u=n(u),R=n(R),t=n(t);function g(h){if(typeof T!="function")return null;var x=new T,l=new T;return(g=function(Q){return Q?l:x})(h)}function o(h,x){if(!x&&h&&h.__esModule)return h;if(h===null||y(h)!=="object"&&typeof h!="function")return{default:h};var l=g(x);if(l&&l.has(h))return l.get(h);var F={},Q=f&&A;for(var z in h)if(z!=="default"&&Object.prototype.hasOwnProperty.call(h,z)){var N=Q?A(h,z):null;N&&(N.get||N.set)?f(F,z,N):F[z]=h[z]}return F.default=h,l&&l.set(h,F),F}function s(h){var x=(0,i.useState)(!1),l=(0,_.default)(x,2),F=l[0],Q=l[1];function z(){Q(!0)}function N(j){var fe=h.width,ue=h.height,te=h.data,U=h.id,M=h.className,K=te.data,X=K===void 0?[]:K,$=te.yName,le={width:"100%"},ae=j==="small"?ue||146:500,Ae;B.default.isEmpty(X)||X.length>1&&(j==="small"?Ae=X&&X.length>10?10:X.length:Ae=X&&X.length>20?20:X.length);var ve={timestamp:{tickCount:Ae}},Ee=j==="small"&&!$?70:"auto";return i.default.createElement("div",{style:fe||le,className:M},i.default.createElement(W.Chart,{height:ae,data:X,scale:ve,forceFit:!0,padding:["auto","auto",Ee,"auto"]},i.default.createElement(W.Legend,{name:"group",offsetY:j==="small"?-12:-10}),i.default.createElement(W.Tooltip,null),i.default.createElement("div",{className:t.default.chartAction},j==="small"?i.default.createElement("div",{style:{fontSize:14,color:"#111"}},te&&te.name,i.default.createElement("div",{style:{fontSize:12,color:"#333",height:15}},te&&te.subName)):i.default.createElement("div",null),j==="small"?i.default.createElement("div",null,i.default.createElement("span",{className:t.default.iconCon,onClick:function(){h.update(te,U)}},i.default.createElement(a.default,{type:"sync-alt"})),i.default.createElement("span",{className:t.default.iconCon,onClick:z},i.default.createElement(a.default,{type:"arrows-alt",className:t.default.changeBig}))):i.default.createElement("div",null,i.default.createElement("span",{className:t.default.iconConBig,onClick:function(){h.update(te,U)}},i.default.createElement(a.default,{type:"sync-alt"})))),i.default.createElement(W.Axis,{name:"timestamp",label:{autoRotate:!(X&&X.length<=6),formatter:function(ne){return(0,R.default)((0,E.default)(ne)).format("HH:mm:ss")}}}),i.default.createElement(W.Axis,{name:$||"value",label:{formatter:function(ne){return $?Number(ne).toFixed(2):ne}}}),$&&i.default.createElement(W.Geom,{type:"line",position:"timestamp*".concat($),shape:"smooth",tooltip:["timestamp*".concat($),function(ie,ne){return{title:u.default.t("Stability").toString(),name:(0,R.default)(ie).format("HH:mm:ss"),value:ne.toFixed(2)}}]})||i.default.createElement(W.Geom,{type:"line",position:"timestamp*value*group",size:2,color:["group",["#7C6AF2","#5C89FF"]],shape:"smooth",tooltip:["timestamp*value*group",function(ie,ne,de){return{title:(0,R.default)(ie).format("HH:mm:ss"),name:de,value:ne}}]}),$&&i.default.createElement(W.Geom,{type:"point",position:"timestamp*".concat($),size:3,shape:"circle",style:{stroke:"#fff",lineWidth:1},tooltip:["timestamp*".concat($),function(ie,ne){return{title:u.default.t("Stability").toString(),name:(0,R.default)(ie).format("HH:mm:ss"),value:ne.toFixed(2)}}]})||i.default.createElement(W.Geom,{type:"point",position:"timestamp*value*group",size:3,shape:"circle",color:["group",["#7C6AF2","#5C89FF"]],style:{stroke:"#fff",lineWidth:1},tooltip:["timestamp*value*group",function(ie,ne,de){return{title:(0,R.default)(ie).format("HH:mm:ss"),name:de,value:ne}}]})))}function b(){Q(!1)}var V=h.data,O=h.loadingVisible;return i.default.createElement(i.Fragment,null,F&&i.default.createElement(p.default,{style:{width:"80%",height:"65%"},title:i.default.createElement("div",null,V&&V.name,i.default.createElement("div",{style:{fontSize:12,color:"#333"}},V&&V.subName)),visible:F,onClose:b,footer:!1},i.default.createElement(r.default,{visible:O,style:{width:"100%"}}," ",V&&N("big")," ")),i.default.createElement(r.default,{visible:O,style:{width:"25%"}},V&&N("small")))}})},19019:function(w,C,e){var d,I,c,y=e(24596),m=e(67394),f=e(93168),T=e(23587),A=e(83452),S=e(95315),E=e(63774),p=e(92937);(function(r,a){if(!0)!(I=[C,e(13488),e(57379),e(81853),e(36939),e(27378),e(66697),e(98784),e(74590),e(61320),e(28619),e(70343)],d=a,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var _})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(r,a,_,i,B,u,R,t,W,n,g,o){"use strict";var s=e(67971);m(r,"__esModule",{value:!0}),r.default=N,a=s(a),_=s(_),i=s(i),B=s(B),u=x(u),R=s(R),t=s(t),W=s(W),n=s(n),g=s(g);function h(b){if(typeof f!="function")return null;var V=new f,O=new f;return(h=function(fe){return fe?O:V})(b)}function x(b,V){if(!V&&b&&b.__esModule)return b;if(b===null||y(b)!=="object"&&typeof b!="function")return{default:b};var O=h(V);if(O&&O.has(b))return O.get(b);var j={},fe=m&&T;for(var ue in b)if(ue!=="default"&&Object.prototype.hasOwnProperty.call(b,ue)){var te=fe?T(b,ue):null;te&&(te.get||te.set)?m(j,ue,te):j[ue]=b[ue]}return j.default=b,O&&O.set(b,j),j}function l(b,V){var O=A(b);if(S){var j=S(b);V&&(j=j.filter(function(fe){return T(b,fe).enumerable})),O.push.apply(O,j)}return O}function F(b){for(var V=1;V<arguments.length;V++){var O=arguments[V]!=null?arguments[V]:{};V%2?l(Object(O),!0).forEach(function(j){(0,_.default)(b,j,O[j])}):E?p(b,E(O)):l(Object(O)).forEach(function(j){m(b,j,T(O,j))})}return b}var Q=B.default.Group,z;function N(b){var V=(0,u.useState)({days:0,hours:0,minutes:0,seconds:0}),O=(0,i.default)(V,2),j=O[0],fe=O[1];(0,u.useEffect)(function(){return z=setInterval(function(){var Ee=ue();fe(Ee)},1e3),function(){z&&clearInterval(z)}},[j]);function ue(){var Ee=b.data;if(!t.default.isEmpty(Ee)){var ie=t.default.get(Ee,"startTime",""),ne=t.default.get(Ee,"endTime",new Date().getTime()),de=(0,n.default)(ie),Y=(0,n.default)(ne),_e=n.default.duration(Y.diff(de)),he=t.default.floor(_e.as("days")),me=t.default.floor(_e.as("hours")),Pe=t.default.floor(_e.as("minutes")),pe=t.default.floor(_e.as("seconds"));return{days:he,hours:me-he*24,minutes:Pe-me*60,seconds:pe-Pe*60}}return{days:0,hours:0,minutes:0,seconds:0}}function te(){return u.default.createElement("span",null,j.days>0?u.default.createElement(u.Fragment,null,u.default.createElement("span",null,j.days),u.default.createElement("span",null,"days")):"",j.hours>0?u.default.createElement(u.Fragment,null,u.default.createElement("span",null,j.hours),u.default.createElement("span",null,"hours")):"",j.minutes>0?u.default.createElement(u.Fragment,null,u.default.createElement("span",null,j.minutes),u.default.createElement("span",null,"mins")):"",u.default.createElement("span",null,j.seconds),u.default.createElement("span",null,"s"))}function U(){var Ee=b.data;if(!t.default.isEmpty(Ee)){var ie=t.default.get(Ee,"activities",[]),ne=t.default.map(ie,function(Be){return t.default.lowerCase(Be.phase)!=="check"&&Be.runResult===o.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED?F(F({},Be),{},{runResult:o.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR}):Be}),de=t.default.filter(ne,function(Be){return Be.state==="RUNNING"}),Y=t.default.groupBy(ne,"runResult"),_e=t.default.size(Y[o.ExperimentConstants.EXPERIMENT_TASK_RESULT_SUCCESS]),he=t.default.size(Y[o.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED]),me=t.default.size(Y[o.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR]),Pe=t.default.size(Y[o.ExperimentConstants.EXPERIMENT_TASK_RESULT_REJECTED]),pe=t.default.size(Y[o.ExperimentConstants.EXPERIMENT_TASK_RESULT_STOPPED]);return{success:_e,failed:he+pe,error:me,wait:t.default.size(ne)-_e-he-me-Pe-pe-t.default.size(de)}}return{success:0,failed:0,error:0,wait:0}}var M=b.data,K=b.scheduler,X=t.default.get(M,"activities",[]),$=t.default.groupBy(X,"state").FINISHED,le=($&&$.length)/(X&&X.length)||0,ae=U(),Ae=!t.default.isNil(K)&&K.schedulerConfig,ve=t.default.get(Ae,"cronExpression","");return u.default.createElement("div",{className:g.default.basicContent},u.default.createElement("div",{className:g.default.title},u.default.createElement(R.default,null,"Basic Information")),ve&&u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"Timed operation")),u.default.createElement("div",{className:g.default.value},ve)),u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"Start time")),u.default.createElement("div",{className:g.default.value},(0,W.default)(M&&M.startTime))),M&&M.endTime&&u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"End Time")),u.default.createElement("div",{className:g.default.value},(0,W.default)(M&&M.endTime))),u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"Drill time")),u.default.createElement("div",{className:g.default.value},te())),u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"Exercise progress")),u.default.createElement("div",{className:g.default.value},u.default.createElement(a.default,{percent:le*100}))),u.default.createElement("div",{className:g.default.basicItem},u.default.createElement("div",{className:g.default.label},u.default.createElement(R.default,null,"Exercise results")),u.default.createElement("div",{className:g.default.value},u.default.createElement(Q,null,u.default.createElement(B.default,{type:"normal",size:"small"},u.default.createElement("span",null,u.default.createElement(R.default,null,"Run successfully"),": ",u.default.createElement("span",{style:{color:"#1E8E3E"}},ae.success))),u.default.createElement(B.default,{type:"normal",size:"small"},u.default.createElement("span",null,u.default.createElement(R.default,null,"Not as expected"),": ",u.default.createElement("span",{style:{color:"#f69b00"}},ae.failed))),u.default.createElement(B.default,{type:"normal",size:"small"},u.default.createElement("span",null,u.default.createElement(R.default,null,"Abnormal"),": ",u.default.createElement("span",{style:{color:"#d93027"}},ae.error))),u.default.createElement(B.default,{type:"normal",size:"small"},u.default.createElement("span",{style:{color:"#151515"}},u.default.createElement(R.default,null,"Wait to run"),": ",ae.wait>=0?ae.wait:0))))))}})},37916:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(76313),e(27378),e(98784),e(17047)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f,T,A,S){"use strict";var E=e(67971);y(m,"__esModule",{value:!0}),m.default=p,f=E(f),T=E(T),A=E(A);function p(r){function a(W){var n;return A.default.isEmpty(W)?[]:(A.default.map(W,function(g){g.phase==="PREPARE"?n="prepare":g.phase==="ATTACK"?n="attack":g.phase==="CHECK"?n="check":g.phase==="RECOVER"&&(n="recover"),g.stage=n,g.id=g.activityId,g.nodeType=S.TASK}),W)}var _=r.data,i=r.onActivitedClick,B=r.onTryAgain,u=r.selectNode,R=r.onCheck,t=A.default.get(_,"activities",[]);return T.default.createElement(f.default,{editable:!1,nodes:a(t),selectedNode:u,onNodeClick:i,onTryAgain:B,running:!0,onCheck:R,permission:r.permission})}})},27638:function(w,C,e){var d,I,c,y=e(24596),m=e(67394),f=e(93168),T=e(23587);(function(A,S){if(!0)!(I=[C,e(32009),e(12955),e(15286),e(92243),e(77809),e(9863),e(47701),e(72153),e(17225),e(81853),e(36939),e(70525),e(27378),e(66697),e(98784),e(60042),e(74590),e(14798),e(51077),e(70343),e(59652),e(14870)],d=S,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var E})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(A,S,E,p,r,a,_,i,B,u,R,t,W,n,g,o,s,h,x,l,F,Q,z){"use strict";var N=e(67971);m(A,"__esModule",{value:!0}),A.default=U,S=N(S),E=N(E),p=N(p),r=N(r),a=N(a),_=N(_),i=N(i),B=N(B),u=N(u),R=N(R),t=N(t),W=N(W),n=V(n),g=N(g),o=N(o),s=N(s),h=N(h),x=N(x),l=N(l);function b(M){if(typeof f!="function")return null;var K=new f,X=new f;return(b=function(le){return le?X:K})(M)}function V(M,K){if(!K&&M&&M.__esModule)return M;if(M===null||y(M)!=="object"&&typeof M!="function")return{default:M};var X=b(K);if(X&&X.has(M))return X.get(M);var $={},le=m&&T;for(var ae in M)if(ae!=="default"&&Object.prototype.hasOwnProperty.call(M,ae)){var Ae=le?T(M,ae):null;Ae&&(Ae.get||Ae.set)?m($,ae,Ae):$[ae]=M[ae]}return $.default=M,X&&X.set(M,$),$}var O=t.default.Group,j={border:"1px solid #1E8E3E",background:"rgba(30,142,62,0.10)"},fe={border:"1px solid #D93026",background:"rgba(217,48,38,0.10)"},ue={border:"1px solid #F69C00",background:"rgba(246,156,0,0.10)"},te={border:"1px solid #cccccc",background:"#cccccc75"};function U(M){var K=(0,z.useDispatch)(),X=(0,n.useState)(!1),$=(0,R.default)(X,2),le=$[0],ae=$[1],Ae=(0,n.useState)("run"),ve=(0,R.default)(Ae,2),Ee=ve[0],ie=ve[1],ne=(0,n.useState)(""),de=(0,R.default)(ne,2),Y=de[0],_e=de[1],he=(0,n.useState)({}),me=(0,R.default)(he,2),Pe=me[0],pe=me[1],Be=(0,n.useState)(!1),We=(0,R.default)(Be,2),Te=We[0],Je=We[1],pt=(0,n.useState)(!1),Ke=(0,R.default)(pt,2),gt=Ke[0],Ct=Ke[1],ht=(0,n.useState)(!1),je=(0,R.default)(ht,2),xe=je[0],Xe=je[1],vt=(0,n.useState)(null),Ye=(0,R.default)(vt,2),Qe=Ye[0],xt=Ye[1],Rt=(0,n.useState)(null),ze=(0,R.default)(Rt,2),Re=ze[0],qe=ze[1],et=(0,n.useRef)(null),we=(0,z.useSelector)(function(H){var L=H.loginUser;return L}),Bt=(0,n.useState)(""),He=(0,R.default)(Bt,2),tt=He[0],nt=He[1];function Pt(H){return H==="SUCCESS"?j:H==="FAILED"?fe:H==="REJECTED"?te:ue}var at=function(){var L=et.current;if(!L)return;Ge(L.taskId)};function St(H){return H==="SUCCESS"?"green":H==="FAILED"?"red":H==="REJECTED"?"#cccccc":"yellow"}function lt(H){ae(!0),ie("run");var L=n.default.createElement(i.default,{className:l.default.logTabs,triggerType:"click",onChange:function(q){return ie(q)},extra:n.default.createElement(B.default,{type:"primary",onClick:function(){return at()},style:{margin:"-12px 32px 0 0",fontSize:"14px"},text:!0},n.default.createElement(u.default,{type:"refresh"})," \u5237\u65B0")},n.default.createElement(i.default.Item,{key:"run",title:x.default.t("Machine execution information").toString()}));_e(L),pe(H)}function it(){if(Te){var H=M.clearCurrentActivity;Je(!1),H&&H()}ae(!1),Ct(!1)}function rt(H){try{var L=(0,S.default)(JSON.parse(H),null,2);return L}catch(J){console.log(J)}return H}function It(){var H=M.paramer,L=M.data,J=M.currentActivity,q=o.default.isEmpty(J)?L:H;if(Te&&!o.default.isEmpty(q))return!o.default.isEmpty(q&&q.arguments)&&q.arguments.map(function(oe){return n.default.createElement(W.default,{key:oe.parameterId,parameter:oe,disabled:!0,width:800,isSwitch:!1})});if(le&&!o.default.isEmpty(Pe))return yt()}var ut=function(){var L=Qe||{},J=L.extraInfo,q=J||{},oe=q.origin_request,se=q.origin_response,Ne=n.default.createElement("pre",{className:l.default.infoContent},"\u65E0");return!oe&&!se?n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Debug info")),Ne):n.default.createElement("div",null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Debug info")),n.default.createElement("div",{style:{marginLeft:"16px",marginTop:"8px"}},n.default.createElement("strong",null,n.default.createElement(g.default,null,"Request data"),": "),oe&&n.default.createElement("pre",{className:l.default.infoContent,dangerouslySetInnerHTML:{__html:(0,S.default)(JSON.parse(oe),null,4)}})||Ne,n.default.createElement("strong",null,n.default.createElement(g.default,null,"Response data"),": "),se&&n.default.createElement("pre",{className:l.default.infoContent,dangerouslySetInnerHTML:{__html:(0,S.default)(JSON.parse(se),null,4)}})||Ne))},yt=function(){if(Ee==="run"){var L=Qe||{},J=o.default.get(L,"startTime",""),q=o.default.get(L,"endTime",""),oe=o.default.get(L,"solution","");return n.default.createElement(_.default,{visible:xe,style:{display:"block"}},n.default.createElement("div",{className:l.default.infoContainer},Re&&n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Basic Information")),n.default.createElement("div",{className:l.default.detailInfo},n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"Machine name"),": "),Re.deviceName),n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"IP address"),": "),Re.deviceType===0&&n.default.createElement("a",{target:"_blank",href:"".concat(location.origin,"/chaos/experiment/scope/detail?id=").concat(Re.deviceConfigurationId)},Re.ip," ",n.default.createElement(u.default,{type:"external-link",size:"xs"}))||Re.ip),n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"Container name"),": "),Re.ip),n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"Machine type"),": "),Re.type),Re.clusterName&&n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"Cluster"),": "),Re.clusterName),Re.kubNamespace&&n.default.createElement("div",null,n.default.createElement("span",null,"namepace: "),Re.kubNamespace),n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"Start time"),": "),(0,h.default)(J)),q&&n.default.createElement("div",null,n.default.createElement("span",null,n.default.createElement(g.default,null,"End Time"),": "),(0,h.default)(q)))),L.data&&!o.default.isNil(L.data)&&n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Information")),n.default.createElement("div",{className:l.default.infoContent},n.default.createElement("pre",null,rt(L.data)))),L.errorMessage&&n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Mistake")),n.default.createElement("div",{className:l.default.infoContent},n.default.createElement(g.default,null,"Reason"),": ",L.errorMessage)),oe&&n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:l.default.infoTitle},n.default.createElement(g.default,null,"Check")),n.default.createElement("pre",{className:l.default.infoContent,dangerouslySetInnerHTML:{__html:oe}})),(we==null?void 0:we.isAdmin)&&ut()))}},Ge=function(){var H=(0,a.default)(regeneratorRuntime.mark(function L(J){var q;return regeneratorRuntime.wrap(function(se){for(;;)switch(se.prev=se.next){case 0:return Xe(!0),se.next=3,K.experimentTask.QueryMiniAppTask({miniAppTaskId:J});case 3:q=se.sent,Xe(!1),q&&xt(q);case 6:case"end":return se.stop()}},L)}));return function(J){return H.apply(this,arguments)}}(),Ut=function(){var H=(0,a.default)(regeneratorRuntime.mark(function L(J){var q;return regeneratorRuntime.wrap(function(se){for(;;)switch(se.prev=se.next){case 0:return se.next=2,K.experimentTask.QueryMiniAppTaskInfo({appConfigurationId:J});case 2:q=se.sent,q&&qe(q);case 4:case"end":return se.stop()}},L)}));return function(J){return H.apply(this,arguments)}}(),Ze=function(L){Ge(L.taskId),Ut(L.appConfigurationId),lt(L),et.current=L},Tt=function(L,J,q){return n.default.createElement(r.default,{key:J,trigger:q===1?n.default.createElement(t.default,{key:J,color:St(L.result),style:{marginRight:8,marginBottom:8,cursor:"pointer"},onClick:function(){return Ze(L)},size:"small"},L.hostIp):n.default.createElement("div",{className:l.default.content,style:Pt(L.result),onClick:function(){return Ze(L)}}),closable:!1,align:"t"},L.hostIp)};function dt(H){var L=H.filter(function(q){var oe=q.deviceName,se=q.hostIp;return!tt||(oe+" "+se).indexOf(tt)!==-1}),J=L.length>12?0:1;return n.default.createElement("div",{className:l.default.ipsContent},H.length>12&&n.default.createElement(p.default,{hasClear:!0,placeholder:x.default.t("Fuzzy search...").toString(),className:l.default.ipsSearch,onChange:function(oe){return nt(o.default.trim(oe))}}),n.default.createElement("div",{className:l.default.ips},L.map(function(q,oe){return Tt(q,oe,J)})))}function Dt(){var H=M.data,L=H&&H.apps,J=o.default.groupBy(L,"result"),q=J.SUCCESS&&J.SUCCESS.length,oe=J.FAILED&&J.FAILED.length,se=J.READY&&J.READY.length,Ne=J.REJECTED&&J.REJECTED.length,Oe=H&&H.userCheckState;return n.default.createElement("div",{className:l.default.machine},n.default.createElement(O,null,n.default.createElement(t.default,{type:"normal",size:"small"},n.default.createElement(g.default,null,"Success"),": ",n.default.createElement("span",{className:l.default.success},q||0)),n.default.createElement(t.default,{type:"normal",size:"small"},n.default.createElement(g.default,null,"Fail"),": ",n.default.createElement("span",{className:l.default.faile},oe||0)),n.default.createElement(t.default,{type:"normal",size:"small"},n.default.createElement(g.default,null,"Skip execution"),": ",n.default.createElement("span",{style:{color:"grey"}},Ne||0)),n.default.createElement(t.default,{type:"normal",size:"small"},n.default.createElement(g.default,null,"Wait to run"),": ",se||0)),n.default.createElement("div",{className:l.default.machineDetail},n.default.createElement("div",{className:l.default.detailTop},n.default.createElement("div",{className:l.default.iconAndWord},n.default.createElement(u.default,{type:"cloud-machine",className:l.default.titleIcon}),n.default.createElement("div",{className:l.default.title},n.default.createElement(g.default,null,"Machine"),"\uFF08",n.default.createElement("span",null,L&&L.length),"\uFF09")),n.default.createElement("div",{className:l.default.iconAndWord},n.default.createElement(u.default,{type:"help",className:l.default.tipsIcon}),n.default.createElement("div",{className:l.default.tips},n.default.createElement(g.default,null,"Click on the machine to view details")))),L&&dt(L)),n.default.createElement("div",{className:l.default.userCheckState},n.default.createElement("div",{className:l.default.checkStateLabel},n.default.createElement(g.default,null,"User confirmation result")),Oe===F.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED&&n.default.createElement("div",{className:l.default.checkPass},n.default.createElement(g.default,null,"Continue implement")),Oe===F.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED&&n.default.createElement("div",{className:l.default.checkFailed},n.default.createElement(g.default,null,"Terminate the drill")),(!Oe||Oe===F.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING)&&n.default.createElement("div",null,"-")))}function ot(){var H=M.data,L=M.currentActivity,J=M.paramer,q=o.default.isEmpty(L)?H:J,oe=o.default.get(q,"arguments",[]);if(o.default.isEmpty(oe))return;var se=o.default.get(q,"miniAppName","");Je(!0),_e("".concat(se).concat(x.default.t("Node execution parameters").toString()))}var Fe=M.data,Ve=M.activity,Nt=M.chartMetric,st=M.currentActivity,bt=M.paramer,ft=o.default.get(Ve,"activityTaskId",""),_t=o.default.get(Ve,"miniAppCode",""),$e=(0,Q.getPlugin)(_t,{code:_t,data:Nt,id:ft}),Mt=o.default.isEmpty(st)?Fe:bt,De=o.default.get(Mt,"arguments",[]),Et;return De.length||(Et=l.default.noParamsStyle),n.default.createElement("div",{className:l.default.infoContent},n.default.createElement(i.default,{shape:"capsule",size:"small",defaultActiveKey:1},n.default.createElement(i.default.Item,{title:"\u673A\u5668\u4FE1\u606F",key:1},!o.default.isEmpty(Fe)&&Dt()),n.default.isValidElement($e)&&!o.default.isEmpty(Fe)?n.default.createElement(i.default.Item,{title:x.default.t("Execution dynamics").toString(),key:2},n.default.createElement("div",null,$e)):null),n.default.createElement("div",{className:l.default.actionCon},!o.default.isEmpty(Fe)&&n.default.createElement("span",{className:(0,s.default)(l.default.action,Et),onClick:ot,title:De.length?"":x.default.t("This node has no parameter configuration").toString()},n.default.createElement(g.default,null,"Parameter"))),n.default.createElement(E.default,{title:Y,className:l.default.infoDialog,style:{width:"90%"},visible:Te||gt||le,footer:!1,onOk:it,onClose:it},It()))}})},7177:function(w,C,e){var d,I,c,y=e(24596),m=e(67394),f=e(93168),T=e(23587);(function(A,S){if(!0)!(I=[C,e(42499),e(17225),e(12955),e(72153),e(77809),e(17534),e(81853),e(78583),e(53369),e(26650),e(27378),e(19019),e(37916),e(27638),e(66697),e(98784),e(74590),e(14798),e(34912),e(70343),e(96291),e(17640),e(99328),e(14870),e(42058)],d=S,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var E})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(A,S,E,p,r,a,_,i,B,u,R,t,W,n,g,o,s,h,x,l,F,Q,z,N,b,V){"use strict";var O=e(67971);m(A,"__esModule",{value:!0}),A.default=te,S=O(S),E=O(E),p=O(p),r=O(r),a=O(a),_=O(_),i=O(i),B=O(B),u=O(u),R=O(R),t=fe(t),W=O(W),n=O(n),g=O(g),o=O(o),s=O(s),h=O(h),x=O(x),l=O(l);function j(U){if(typeof f!="function")return null;var M=new f,K=new f;return(j=function($){return $?K:M})(U)}function fe(U,M){if(!M&&U&&U.__esModule)return U;if(U===null||y(U)!=="object"&&typeof U!="function")return{default:U};var K=j(M);if(K&&K.has(U))return K.get(U);var X={},$=m&&T;for(var le in U)if(le!=="default"&&Object.prototype.hasOwnProperty.call(U,le)){var ae=$?T(U,le):null;ae&&(ae.get||ae.set)?m(X,le,ae):X[le]=U[le]}return X.default=U,K&&K.set(U,X),X}var ue=function(){console.log()};function te(){var U=(0,b.useDispatch)(),M=(0,V.useHistory)(),K,X=(0,b.useSelector)(function(v){var P=v.experimentTask;return P.dependenceSubmit}),$=(0,b.useSelector)(function(v){var P=v.experimentTask;return P.reStartTaskId}),le=(0,b.useSelector)(function(v){var P=v.experimentTask;return P.stopResult}),ae=(0,b.useSelector)(function(v){return v.loading.effects["experimentTask/retryActivityTask"]}),Ae=(0,t.useState)(!1),ve=(0,i.default)(Ae,2),Ee=ve[0],ie=ve[1],ne=(0,t.useState)(null),de=(0,i.default)(ne,2),Y=de[0],_e=de[1],he=(0,t.useState)(null),me=(0,i.default)(he,2),Pe=me[0],pe=me[1],Be=(0,t.useState)(null),We=(0,i.default)(Be,2),Te=We[0],Je=We[1],pt=(0,t.useState)(null),Ke=(0,i.default)(pt,2),gt=Ke[0],Ct=Ke[1],ht=(0,t.useState)([]),je=(0,i.default)(ht,2),xe=je[0],Xe=je[1],vt=(0,t.useState)([]),Ye=(0,i.default)(vt,2),Qe=Ye[0],xt=Ye[1],Rt=(0,t.useState)(!1),ze=(0,i.default)(Rt,2),Re=ze[0],qe=ze[1],et=(0,t.useState)(!1),we=(0,i.default)(et,2),Bt=we[0],He=we[1],tt=(0,t.useState)(!1),nt=(0,i.default)(tt,2),Pt=nt[0],at=nt[1],St=(0,t.useState)(!1),lt=(0,i.default)(St,2),it=lt[0],rt=lt[1],It=(0,t.useState)(!1),ut=(0,i.default)(It,2),yt=ut[0],Ge=ut[1],Ut=(0,t.useState)(!1),Ze=(0,i.default)(Ut,2),Tt=Ze[0],dt=Ze[1],Dt=(0,t.useState)(!1),ot=(0,i.default)(Dt,2),Fe=ot[0],Ve=ot[1],Nt=(0,t.useState)(!1),st=(0,i.default)(Nt,2),bt=st[0],ft=st[1],_t=(0,t.useState)(null),$e=(0,i.default)(_t,2),Mt=$e[0],De=$e[1],Et=(0,t.useState)(!1),H=(0,i.default)(Et,2),L=H[0],J=H[1],q=(0,t.useState)(null),oe=(0,i.default)(q,2),se=oe[0],Ne=oe[1],Oe=(0,t.useState)(!1),Ot=(0,i.default)(Oe,2),Vt=Ot[0],$t=Ot[1],Jt=(0,t.useState)(!1),Lt=(0,i.default)(Jt,2),wt=Lt[0],Le=Lt[1],qt=(0,t.useState)(!1),kt=(0,i.default)(qt,2),en=kt[0],Ie=kt[1],tn=(0,t.useState)({}),Wt=(0,i.default)(tn,2),At=Wt[0],Kt=Wt[1],nn=(0,t.useState)(!1),jt=(0,i.default)(nn,2),an=jt[0],ln=jt[1],rn=(0,t.useState)(null),Xt=(0,i.default)(rn,2),ke=Xt[0],Yt=Xt[1],un=(0,b.useSelector)(function(v){return{reRunLoading:v.loading.effects["experimentTask/runExperiment"]}}),dn=un.reRunLoading;(0,t.useEffect)(function(){if(s.default.isBoolean(le)&&!le&&(U.experimentTask.clearTasksStopResult(),_.default.error(x.default.t("Failed to stop rehearsal"))),$){U.experimentTask.clearExperimentStartingResult(),(0,N.pushUrl)(M,"/chaos/experiment/task",{id:$}),(0,a.default)(regeneratorRuntime.mark(function P(){return regeneratorRuntime.wrap(function(Z){for(;;)switch(Z.prev=Z.next){case 0:return Z.next=2,U.experimentTask.getExperimentTask({taskId:$},function(D){var k=D||{},ee=k.feedbackStatus,ce=k.state;if(!s.default.isEmpty(D)&&_e(D),ce===F.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(Le(!1),(0,a.default)(regeneratorRuntime.mark(function Ce(){return regeneratorRuntime.wrap(function(re){for(;;)switch(re.prev=re.next){case 0:return re.next=2,U.experimentTask.getExperimentTaskFeedback({taskId:$});case 2:case"end":return re.stop()}},Ce)}))(),ee||Ie(!0)),D&&D.activities){var ge=!s.default.isEmpty(D.activities)&&D.activities[0].activityTaskId;pe(!s.default.isEmpty(D.activities)&&D.activities[0]),mt(ge),(0,a.default)(regeneratorRuntime.mark(function Ce(){return regeneratorRuntime.wrap(function(re){for(;;)switch(re.prev=re.next){case 0:return re.next=2,U.experimentTask.getTaskMetric({activityTaskId:ge},function(Ue){s.default.isEmpty(Ue)||De(Ue)});case 2:case"end":return re.stop()}},Ce)}))()}});case 2:case"end":return Z.stop()}},P)}))(),ct($);var v=s.default.get(Pe,"activityTaskId","");(0,a.default)(regeneratorRuntime.mark(function P(){return regeneratorRuntime.wrap(function(Z){for(;;)switch(Z.prev=Z.next){case 0:return Z.next=2,U.experimentTask.getTaskMetric({activityTaskId:v},function(D){De(D)});case 2:case"end":return Z.stop()}},P)}))()}});var mt=function(){var v=(0,a.default)(regeneratorRuntime.mark(function P(G,Z){return regeneratorRuntime.wrap(function(k){for(;;)switch(k.prev=k.next){case 0:return k.next=2,U.experimentTask.getActivityTask({activityTaskId:G},function(ee){s.default.isEmpty(ee)||(Je(ee),Z&&Ct(ee))});case 2:case"end":return k.stop()}},P)}));return function(G,Z){return v.apply(this,arguments)}}(),ct=function(){var v=(0,a.default)(regeneratorRuntime.mark(function P(G){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.getExperiementTaskGuardInfo({taskId:G},function(k){s.default.isEmpty(k)||(Xe(k&&k.metrics),xt(k&&k.strategies))});case 2:case"end":return D.stop()}},P)}));return function(G){return v.apply(this,arguments)}}();(0,t.useEffect)(function(){U.pageHeader.setBreadCrumbItems(Q.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace",value:x.default.t("Space management").toString(),path:"/chaos/workspace/list"},{key:"experiment_task",value:x.default.t("Drill Record Details").toString(),path:"/chaos/experiment/task"}]));var v=be();s.default.isEmpty(v)||((0,a.default)(regeneratorRuntime.mark(function P(){return regeneratorRuntime.wrap(function(Z){for(;;)switch(Z.prev=Z.next){case 0:return Z.next=2,U.experimentTask.getExperimentTask({taskId:v},function(D){!D&&Le(!1);var k=D||{},ee=k.feedbackStatus,ce=k.state;if(!s.default.isEmpty(D)&&_e(D),ce===F.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(Le(!1),(0,a.default)(regeneratorRuntime.mark(function Ce(){return regeneratorRuntime.wrap(function(re){for(;;)switch(re.prev=re.next){case 0:return re.next=2,U.experimentTask.getExperimentTaskFeedback({taskId:v});case 2:case"end":return re.stop()}},Ce)}))(),ee||Ie(!0)),D&&D.activities){var ge=!s.default.isEmpty(D.activities)&&D.activities[0].activityTaskId;pe(!s.default.isEmpty(D.activities)&&D.activities[0]),mt(ge),(0,a.default)(regeneratorRuntime.mark(function Ce(){return regeneratorRuntime.wrap(function(re){for(;;)switch(re.prev=re.next){case 0:return re.next=2,U.experimentTask.getTaskMetric({activityTaskId:ge},function(Ue){s.default.isEmpty(Ue)||De(Ue)});case 2:case"end":return re.stop()}},Ce)}))()}else D&&D.code&&Le(!1)});case 2:case"end":return Z.stop()}},P)}))(),ct(v))},[]),(0,t.useEffect)(function(){var v=zt(Y)||!1,P=Ft(Y);s.default.isEmpty(Y)||(U.pageHeader.setTitle(Y.experimentName),U.pageHeader.setHeaderExtra(t.default.createElement(r.default.Group,{style:{float:"right"}},t.default.createElement(r.default,{type:"normal",loading:dn,disabled:!P||!(0,z.handleIsAdmin)(Y==null?void 0:Y.permission,4),onClick:on},t.default.createElement(o.default,null,"Do it again")),t.default.createElement(r.default,{type:"primary",className:l.default.buttonSecond,onClick:sn},t.default.createElement(o.default,null,"See details")),!P&&t.default.createElement(r.default,{type:"primary",disabled:v||!(0,z.handleIsAdmin)(Y==null?void 0:Y.permission,4),warning:!v,onClick:fn},v?x.default.t("Stoppings").toString():x.default.t("Termination").toString())))),!s.default.isEmpty(Y)&&Ft(Y)?(Ee&&_n(),Vt||$t(!0)):Ee||(ie(!0),Le(!0))}),(0,t.useEffect)(function(){return wt&&Qt(),function(){K&&clearTimeout(K)}},[Ee,wt,ke]);function Qt(){var v,P=be();if((0,a.default)(regeneratorRuntime.mark(function G(){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.getExperimentTask({taskId:P},function(k){var ee=k||{},ce=ee.feedbackStatus,ge=ee.state;!s.default.isEmpty(k)&&_e(k),ge===F.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&((0,a.default)(regeneratorRuntime.mark(function Ce(){return regeneratorRuntime.wrap(function(re){for(;;)switch(re.prev=re.next){case 0:return re.next=2,U.experimentTask.getExperimentTaskFeedback({taskId:P},function(Ue){!s.default.isEmpty(Ue)&&Kt(Ue)});case 2:case"end":return re.stop()}},Ce)}))(),ce||Ie(!0)),k&&k.activities&&(k.activities[0].state===F.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(ke?v=ke&&ke.activityTaskId:v=!s.default.isEmpty(k.activities)&&k.activities[0].activityTaskId,mt(v)))});case 2:case"end":return D.stop()}},G)}))(),ct(P),K&&clearTimeout(K),!wt)return;K=setTimeout(function(){Qt()},3e3)}function on(){var v=be();if(!s.default.isEmpty(Y)&&Ft(Y)){var P=s.default.get(Y,"experimentId","");s.default.isEmpty(P)||((0,a.default)(regeneratorRuntime.mark(function G(){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.runExperiment({experimentId:P});case 2:case"end":return D.stop()}},G)}))(),ct(v))}}function sn(){var v=s.default.get(Y,"experimentId","");s.default.isEmpty(v)||(0,N.pushUrl)(M,"/chaos/experiment/detail",{id:v})}function fn(){s.default.isEmpty(Y)||(zt(Y)?p.default.alert({title:x.default.t("Repeat action reminder").toString(),content:x.default.t("The current drill is being stopped, please do not repeat the operation").toString(),onOk:ue}):p.default.confirm({title:x.default.t("Stop the drill").toString(),content:x.default.t("Stop the currently running exercise").toString(),locale:{ok:x.default.t("Confirm"),cancel:x.default.t("Cancel")},onOk:function(){var P=be();(0,a.default)(regeneratorRuntime.mark(function G(){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.stopExperimentTask({taskId:P});case 2:case"end":return D.stop()}},G)}))()},onCancel:ue}))}function _n(){clearTimeout(K),ie(!1),Le(!1)}function be(){var v=(0,N.parseQuery)(),P=v.id;return P}function Ft(v){if(!s.default.isEmpty(v)){var P=v.state;return P===F.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED}return!1}function zt(v){if(!s.default.isEmpty(v)){var P=v.state;return P===F.ExperimentConstants.EXPERIMENT_TASK_STATE_STOPPING}return!1}function ye(v,P){switch(P){case 0:qe(!0);break;case 1:He(!0);break;case 2:at(!0);break;case 3:rt(!0);break;case 4:Ge(!0);break;case 5:dt(!0);break;case 6:Ve(!0);break;case 7:ft(!0);break;default:break}var G=be();(0,a.default)(regeneratorRuntime.mark(function Z(){return regeneratorRuntime.wrap(function(k){for(;;)switch(k.prev=k.next){case 0:return k.next=2,U.experimentTask.getExperiementTaskGuardInfo({taskId:G},function(ee){s.default.isEmpty(ee)||(ee&&ee.metrics.forEach(function(ce){ce.guardId===v.guardId&&xe.forEach(function(ge,Ce){ge.guardId===ce.guardId&&(xe[Ce]=ce)})}),Xe(xe),qe(!1),He(!1),at(!1),rt(!1),Ge(!1),dt(!1),Ve(!1),ft(!1))});case 2:case"end":return k.stop()}},Z)}))()}function En(){J(!0)}function An(){var v=s.default.get(se,"fields",[]),P=s.default.get(se,"tolerance",[]);return t.default.createElement("div",null,t.default.createElement("div",{className:l.default.showStrategy},t.default.createElement(o.default,null,"Rules")),t.default.createElement("div",{className:l.default.showRules},!s.default.isEmpty(v)&&v.map(function(G,Z){var D=s.default.findKey(G,function(ee){return ee===!0}),k;return D==="and"?k=x.default.t("And"):k=x.default.t("Or"),t.default.createElement("span",null,G.name,G.operation&&G.operation.label+G.value+G.unit,"\xA0\xA0",Z<v.length-1?k:"","\xA0\xA0")})),t.default.createElement("div",{className:l.default.showStrategy},t.default.createElement(o.default,null,"Recovery strategy")),t.default.createElement("div",{className:l.default.showTolerance},!s.default.isEmpty(P)&&P.map(function(G){return t.default.createElement("div",{className:l.default.tolerance},G.name,"\xA0\xA0",G.value+G.unit)})))}var mn=function(P,G,Z){return Ne(Z),t.default.createElement("span",{className:l.default.ruleDetail,onClick:En},t.default.createElement(o.default,null,"Rules Details"))},cn=function(P){if(P==="RUNNING"||P==="READY")return t.default.createElement("div",{className:l.default.iconContent},t.default.createElement("div",{className:l.default.circle}),t.default.createElement("img",{src:"https://img.alicdn.com/tfs/TB1znK5VRr0gK0jSZFnXXbRRXXa-14-16.svg",className:l.default.svg}));if(P==="TRIGGERED")return t.default.createElement("div",{className:l.default.triggered},t.default.createElement(E.default,{type:"warning",className:l.default.svgFail}),t.default.createElement("div",null,t.default.createElement(o.default,null,"Automatic termination")));if(P==="FINISHED")return t.default.createElement("div",{className:l.default.iconContent},t.default.createElement("div",{className:l.default.circleStill}),t.default.createElement("img",{src:"https://img.alicdn.com/tfs/TB1znK5VRr0gK0jSZFnXXbRRXXa-14-16.svg",className:l.default.svg}))},pn=function(P,G,Z){var D=Z.tolerance,k=D===void 0?[]:D;return!s.default.isEmpty(k)&&k.map(function(ee,ce){return t.default.createElement("span",{className:l.default.tolerance},ee.name,ee.value+ee.unit,ce!==k.length-1?"\uFF1B":null)})};function gn(v){pe(v),Yt(v);var P=v.activityTaskId;mt(P,!0),(0,a.default)(regeneratorRuntime.mark(function G(){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.getTaskMetric({activityTaskId:P},function(k){De(k)});case 2:case"end":return D.stop()}},G)}))()}function Cn(){var v=s.default.get(Y,"source","");return v===1?t.default.createElement(r.default.Group,null,t.default.createElement(r.default,{type:"primary",onClick:function(){return Me(!0)}},t.default.createElement(o.default,null,"OK, return to strong and weak dependency governance")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Me(!1)}},t.default.createElement(o.default,null,"Ok, stay on this page"))):v===2?t.default.createElement(r.default.Group,null,t.default.createElement(r.default,{type:"primary",onClick:function(){return Me(!0)}},t.default.createElement(o.default,null,"OK, return to message walkthrough")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Me(!1)}},t.default.createElement(o.default,null,"Ok, stay on this page")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Ie(!1)}},t.default.createElement(o.default,null,"Cancel, next feedback"))):v===3?t.default.createElement(r.default.Group,null,t.default.createElement(r.default,{type:"primary",onClick:function(){return Me(!0)}},t.default.createElement(o.default,null,"OK, return to disaster recovery drill")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Me(!1)}},t.default.createElement(o.default,null,"Ok, stay on this page")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Ie(!1)}},t.default.createElement(o.default,null,"Cancel, next feedback"))):t.default.createElement(r.default.Group,null,t.default.createElement(r.default,{type:"primary",onClick:function(){return Me(!0)}},t.default.createElement(o.default,null,"Confirm")),t.default.createElement(r.default,{type:"normal",className:l.default.normalBtn,onClick:function(){return Ie(!1)}},t.default.createElement(o.default,null,"Cancel, next feedback")))}function Me(v){var P=be();(0,a.default)(regeneratorRuntime.mark(function G(){return regeneratorRuntime.wrap(function(D){for(;;)switch(D.prev=D.next){case 0:return D.next=2,U.experimentTask.submitExperimentTaskFeedback({feedback:X,taskId:P},function(k){Ie(!1),s.default.isEmpty(k)||(v&&(k.source===1||k.source===2||k.source===3)?window.location.href=k.redirectUrl:(_.default.success(x.default.t("Feedback success")),(0,a.default)(regeneratorRuntime.mark(function ee(){return regeneratorRuntime.wrap(function(ge){for(;;)switch(ge.prev=ge.next){case 0:return ge.next=2,U.experimentTask.getExperimentTaskFeedback({taskId:P},function(Ce){!s.default.isEmpty(Ce)&&Kt(Ce),!s.default.isEmpty(Ce)&&ln(!0)});case 2:case"end":return ge.stop()}},ee)}))()))});case 2:case"end":return D.stop()}},G)}))()}function hn(v){U.experimentTask.setExtraChange(v)}function vn(){return t.default.createElement("div",{className:l.default.specialDom},t.default.createElement(R.default,{data:X,onSpecialDomChange:hn}))}function xn(v){U.experimentTask.setFeedBackChange(v)}function Rn(v){var P=s.default.get(v,"value",""),G=s.default.get(v,"format.options");return G?s.default.find(G,function(Z){return Z.key===P})&&s.default.find(G,function(Z){return Z.key===P}).value:P}function Bn(v,P){var G=be(),Z=v.activityTaskId;s.default.isEmpty(v)||(0,a.default)(regeneratorRuntime.mark(function D(){return regeneratorRuntime.wrap(function(ee){for(;;)switch(ee.prev=ee.next){case 0:return ee.next=2,U.experimentTask.retryActivityTask({activityTaskId:Z},function(ce){P(ce),ce&&(ce&&ce.success&&(0,a.default)(regeneratorRuntime.mark(function ge(){return regeneratorRuntime.wrap(function(Se){for(;;)switch(Se.prev=Se.next){case 0:return Se.next=2,U.experimentTask.getExperimentTask({taskId:G},function(re){pe(!s.default.isEmpty(re.activities)&&re.activities[0])});case 2:case"end":return Se.stop()}},ge)}))())});case 2:case"end":return ee.stop()}},D)}))()}function Pn(v,P,G){var Z=P.activityTaskId;(0,a.default)(regeneratorRuntime.mark(function D(){return regeneratorRuntime.wrap(function(ee){for(;;)switch(ee.prev=ee.next){case 0:return ee.next=2,U.experimentTask.userCheckActivityTask({activityTaskId:Z,success:v},G);case 2:case"end":return ee.stop()}},D)}))()}var Sn=function(P){return"".concat(x.default.t("Protect")).concat(P)},Ht=s.default.get(Y,"source",""),In=s.default.get(Y,"feedbackStatus",0),yn=s.default.get(At,"expectationStatus",0),Un=s.default.get(At,"businessStatus",0),Tn=s.default.get(At,"memo",""),Gt=s.default.get(At,"extra.options",[]),Dn=s.default.get(Y,"extInfo",{}),Nn=s.default.get(Te,"startTime",""),Zt=s.default.get(Te,"endTime","");return t.default.createElement("div",{className:l.default.warper},t.default.createElement(W.default,{data:Y,scheduler:Dn}),t.default.createElement("div",{className:l.default.charts},!s.default.isEmpty(xe)&&t.default.createElement(t.default.Fragment,null,t.default.createElement(B.default,{data:xe[0],update:ye,loadingVisible:Re,id:0}),t.default.createElement(B.default,{data:xe[1],update:ye,loadingVisible:Bt,id:1}),t.default.createElement(B.default,{data:xe[2],update:ye,loadingVisible:Pt,id:2}),t.default.createElement(B.default,{data:xe[3],update:ye,loadingVisible:it,id:3}),t.default.createElement(B.default,{data:xe[4],update:ye,loadingVisible:yt,id:4}),t.default.createElement(B.default,{data:xe[5],update:ye,loadingVisible:Tt,id:5}),t.default.createElement(B.default,{data:xe[6],update:ye,loadingVisible:Fe,id:6}),t.default.createElement(B.default,{data:xe[7],update:ye,loadingVisible:bt,id:7}))),t.default.createElement("div",{className:l.default.line}),t.default.createElement("div",{className:l.default.strategies},t.default.createElement("div",{className:l.default.titleTips},t.default.createElement(o.default,null,"Protection strategy")),!s.default.isEmpty(Qe)&&t.default.createElement(S.default,{hasBorder:!1,dataSource:Qe},t.default.createElement(S.default.Column,{title:x.default.t("Policy name").toString(),dataIndex:"name",cell:Sn}),t.default.createElement(S.default.Column,{title:x.default.t("Policy status").toString(),dataIndex:"state",cell:cn}),t.default.createElement(S.default.Column,{title:x.default.t("Policy content").toString(),dataIndex:"tolerance",cell:pn}),t.default.createElement(S.default.Column,{title:x.default.t("Operation").toString(),cell:mn}))),t.default.createElement("div",{className:l.default.taskDetail},t.default.createElement("div",{className:l.default.titleTips},t.default.createElement(o.default,null,"Implementation")),t.default.createElement(_.default,{type:"notice"},t.default.createElement("div",{className:l.default.timeRange},t.default.createElement("div",null,t.default.createElement("span",{className:l.default.start},t.default.createElement(o.default,null,"Node start execution time"),": ",(0,h.default)(Nn)),Zt&&t.default.createElement("span",null,t.default.createElement(o.default,null,"Node end execution time"),": ",(0,h.default)(Zt))))),t.default.createElement("div",{className:l.default.taskContent},t.default.createElement("div",{className:l.default.flows},ae?t.default.createElement("div",{className:l.default.flowLoading},t.default.createElement(E.default,{type:"loading",size:"xl"})):s.default.isEmpty(Y)?t.default.createElement("div",null,t.default.createElement(o.default,null,"No data")):t.default.createElement(n.default,{data:Y,selectNode:Pe,onActivitedClick:gn,onTryAgain:Bn,onCheck:Pn,permission:Y==null?void 0:Y.permission})),t.default.createElement("div",{className:l.default.taskInfo},t.default.createElement(g.default,{data:Te,activitiing:ke,paramer:gt,currentActivity:ke,activity:Pe,chartMetric:Mt||[],clearCurrentActivity:function(){Yt(null)}})))),(In===1||an)&&t.default.createElement("div",{className:l.default.feedBack},t.default.createElement("div",{className:l.default.feedTitle},t.default.createElement(o.default,null,"Result feedback")),t.default.createElement("div",{className:l.default.feedItem},t.default.createElement("div",{className:l.default.label},t.default.createElement(o.default,null,"Business impact")),t.default.createElement("div",{className:l.default.value},Un?x.default.t("Influence").toString():x.default.t("Does not affect").toString())),t.default.createElement("div",{className:l.default.feedItem},t.default.createElement("div",{className:l.default.label},t.default.createElement(o.default,null,"In conclusion")),t.default.createElement("div",{className:l.default.value},yn?x.default.t("In line with expectations").toString():x.default.t("Not as expected").toString())),!s.default.isEmpty(Gt)&&Gt.map(function(v){return t.default.createElement("div",{className:l.default.feedItem},t.default.createElement("div",{className:l.default.label},v&&v.description),t.default.createElement("div",{className:l.default.value},Rn(v)))}),t.default.createElement("div",{className:l.default.feedItem},t.default.createElement("div",{className:l.default.label},t.default.createElement(o.default,null,"Illustrate")),t.default.createElement("div",{className:l.default.value},Tn))),t.default.createElement(p.default,{title:"".concat(se&&se.name).concat(x.default.t("Rules")),style:{width:640},visible:L,footerActions:["ok"],onOk:function(){J(!1)},onClose:function(){J(!1)}},An()),t.default.createElement(p.default,{visible:en,title:x.default.t("Result feedback").toString(),footer:Cn(),className:l.default.dependenceDialog,onClose:function(){Ie(!1)}},t.default.createElement("div",{className:l.default.DialogFrom},t.default.createElement(u.default,{data:Y,value:X,onFormChange:xn,specialDom:(Ht===1||Ht===2)&&vn}))))}})},69483:function(w,C,e){var d,I,c,y=e(24596),m=e(67394),f=e(93168),T=e(23587);(function(A,S){if(!0)!(I=[C,e(12955),e(17225),e(77809),e(81853),e(27378),e(66697),e(39151),e(98784),e(60042),e(14870)],d=S,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var E})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(A,S,E,p,r,a,_,i,B,u,R){"use strict";var t=e(67971);m(A,"__esModule",{value:!0}),A.default=g,S=t(S),E=t(E),p=t(p),r=t(r),a=n(a),_=t(_),i=t(i),B=t(B),u=t(u);function W(o){if(typeof f!="function")return null;var s=new f,h=new f;return(W=function(l){return l?h:s})(o)}function n(o,s){if(!s&&o&&o.__esModule)return o;if(o===null||y(o)!=="object"&&typeof o!="function")return{default:o};var h=W(s);if(h&&h.has(o))return h.get(o);var x={},l=m&&T;for(var F in o)if(F!=="default"&&Object.prototype.hasOwnProperty.call(o,F)){var Q=l?T(o,F):null;Q&&(Q.get||Q.set)?m(x,F,Q):x[F]=o[F]}return x.default=o,h&&h.set(o,x),x}function g(o){var s=(0,R.useDispatch)(),h=(0,a.useState)(o.convertChartData),x=(0,r.default)(h,2),l=x[0],F=x[1],Q=(0,a.useState)(!1),z=(0,r.default)(Q,2),N=z[0],b=z[1],V=(0,a.useState)(""),O=(0,r.default)(V,2),j=O[0],fe=O[1],ue=(0,a.useState)(!1),te=(0,r.default)(ue,2),U=te[0],M=te[1],K=(0,a.useRef)(),X=(0,a.useRef)();(0,a.useEffect)(function(){var _e=o.chartProps,he=o.data,me=o.renderChart;return K.current=me(he,l,_e),X.current=setInterval(function(){return le("refreshing")},5e3),function(){X&&(clearInterval(X.current),X.current=null),K&&(K.current=null)}},[]),(0,a.useEffect)(function(){var _e=o.chartProps,he=o.data,me=o.renderChart;K.current=me(_e,N,he,l)},[U]);function $(){b(!N)}function le(_e){B.default.throttle(function(){j||(fe(_e),ae())},800)()}function ae(){var _e=o.id,he=o.convertChartData;(0,p.default)(regeneratorRuntime.mark(function me(){return regeneratorRuntime.wrap(function(pe){for(;;)switch(pe.prev=pe.next){case 0:return pe.next=2,s.experimentTask.getTaskMetric({activityTaskId:_e},function(Be){Be&&F(he(Be)),fe("")});case 2:case"end":return pe.stop()}},me)}))()}var Ae=o.chartProps,ve=o.renderChart,Ee=o.data,ie=Ae||{},ne=ie.full,de=ie.width,Y=ie.height;return a.default.createElement(a.Fragment,null,a.default.createElement("div",{className:i.default.root},a.default.createElement("div",{className:i.default.tip},a.default.createElement(_.default,null,"There is a delay in data query, you can click the Refresh button to query")),a.default.createElement("div",{className:ne?(0,u.default)(i.default.buttonGroup,i.default.fullscreenMode):i.default.buttonGroup},ne?"":a.default.createElement("span",{className:i.default.fullscreenBtn,onClick:$},a.default.createElement("span",null,a.default.createElement(_.default,null,"Full screen")),a.default.createElement(E.default,{size:"xs",type:"arrows-alt"})),a.default.createElement("span",{className:i.default.refreshBtn,onClick:function(){K.current=null,le("loading"),M(!U)}},a.default.createElement("span",null,a.default.createElement(_.default,null,"Refresh")),a.default.createElement(E.default,{size:"xs",type:"refresh"}))),a.default.createElement("div",{className:i.default.metric},K),B.default.isEmpty(l)?a.default.createElement("div",{className:i.default.empty,style:{width:de&&de+10,height:Y}},a.default.createElement(_.default,null,"No display data yet")):"",!K&&a.default.createElement("div",{className:(0,u.default)(i.default.empty,i.default.loading),style:{width:de+10,height:Y}},a.default.createElement(E.default,{size:"medium",type:"loading"}))),a.default.createElement(S.default,{visible:N,onClose:$,footer:!1,style:{width:650},className:i.default.DialogContent},a.default.createElement("div",{className:i.default.root},a.default.createElement("div",{className:i.default.tip},a.default.createElement(_.default,null,"There is a delay in data query, you can click the Refresh button to query")),a.default.createElement("div",{className:ne?(0,u.default)(i.default.buttonGroup,i.default.fullscreenMode):i.default.buttonGroup},a.default.createElement("span",{className:i.default.refreshBtn,onClick:function(){K.current=null,le("loading"),M(!U)}},a.default.createElement("span",null,a.default.createElement(_.default,null,"Refresh")),a.default.createElement(E.default,{size:"xs",type:"refresh"}))),a.default.createElement("div",{className:i.default.metric},ve(Ae,N,Ee,l)),B.default.isEmpty(l)?a.default.createElement("div",{className:i.default.empty,style:{width:de&&de+10,height:Y}},a.default.createElement(_.default,null,"No display data yet")):"",!K&&a.default.createElement("div",{className:(0,u.default)(i.default.empty,i.default.loading),style:{width:de+10,height:Y}},a.default.createElement(E.default,{size:"medium",type:"loading"})))))}})},43135:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(93080),e(27378),e(98784),e(73014),e(69483)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f,T,A,S,E){"use strict";var p=e(67971);y(m,"__esModule",{value:!0}),m.default=r,f=p(f),T=p(T),A=p(A),E=p(E);function r(a){var _=a||{},i=_.width,B=_.height,u=_.data;function R(){return T.default.createElement(S.Chart,{height:A.default.isNumber(B)&&B>0?B:320,width:A.default.isNumber(i)&&i>0?i:350,autoFit:!0,data:u,padding:"auto"},T.default.createElement(S.Geom,{type:"interval",position:"host*value"}),T.default.createElement(S.Tooltip,{shared:!0}))}return T.default.createElement(E.default,(0,f.default)({},a,{convertChartData:u,renderChart:R}))}})},40159:function(w,C,e){var d,I,c,y=e(14176),m=e(67394);(function(f,T){if(!0)!(I=[C,e(93080),e(14176),e(69483),e(27378),e(98784),e(61320),e(73014)],d=T,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var A})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(f,T,A,S,E,p,r,a){"use strict";var _=e(67971);m(f,"__esModule",{value:!0}),f.default=B,T=_(T),A=_(A),S=_(S),E=_(E),p=_(p),r=_(r);var i="%";function B(u){function R(n,g){return g===i&&p.default.isNumber(n)?n*100:n}function t(){var n=u.data,g=[];if(!p.default.isEmpty(n)){if(p.default.isEmpty(n))return[];var o=p.default.orderBy(n,["timestamp"]);p.default.map(o,function(s){s={value:R(s.value,s.unit),timestamp:s.timestamp,hostIp:s.host,unit:s.unit},g.push(s)})}return g}function W(n,g){var o=u||{},s=o.data,h=o.chartData,x=n||{},l=x.width,F="";if(!p.default.isEmpty(h)){var Q=h[0];p.default.isEmpty(Q)&&(F=Q.unit)}return E.default.createElement(a.Chart,{height:g?300:175,width:l&&p.default.isNumber(l)&&l>0?l:350,data:s,forceFit:!0,padding:"auto"},E.default.createElement(a.Tooltip,null),E.default.createElement(a.Axis,{name:"timestamp",label:{formatter:function(N){return(0,r.default)((0,A.default)(N)).format("HH:mm")}}}),E.default.createElement(a.Axis,{name:"value",label:{formatter:function(N){var b=N;return p.default.isNumber(N)&&(b=(0,A.default)(N).toFixed(1)),"".concat(b).concat(p.default.defaultTo(F,""))}}}),E.default.createElement(a.Geom,{type:"line",position:"timestamp*value",size:2,color:["group",["#7C6AF2","#5C89FF"]],shape:"smooth",tooltip:["timestamp*value*group",function(z,N,b){return{title:(0,r.default)(z).format("HH:mm:ss"),name:b,value:N}}]}),E.default.createElement(a.Geom,{type:"point",position:"timestamp*value",size:3,shape:"circle",color:["group",["#7C6AF2","#5C89FF"]],style:{stroke:"#fff",lineWidth:1},tooltip:["timestamp*value*group",function(z,N,b){return{title:(0,r.default)(z).format("HH:mm:ss"),name:b,value:N}}]}))}return E.default.createElement(S.default,(0,T.default)({},u,{convertChartData:t,renderChart:W}))}})},59652:function(w,C,e){var d,I,c,y=e(67394),m=e(83452),f=e(95315),T=e(23587),A=e(63774),S=e(92937);(function(E,p){if(!0)!(I=[C,e(57379),e(43135),e(40159),e(27378),e(98784)],d=p,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(E,p,r,a,_,i){"use strict";var B=e(67971);y(E,"__esModule",{value:!0}),E.getPlugin=void 0,p=B(p),r=B(r),a=B(a),_=B(_),i=B(i);function u(s,h){var x=m(s);if(f){var l=f(s);h&&(l=l.filter(function(F){return T(s,F).enumerable})),x.push.apply(x,l)}return x}function R(s){for(var h=1;h<arguments.length;h++){var x=arguments[h]!=null?arguments[h]:{};h%2?u(Object(x),!0).forEach(function(l){(0,p.default)(s,l,x[l])}):A?S(s,A(x)):u(Object(x)).forEach(function(l){y(s,l,T(x,l))})}return s}var t="mkapp",W=["mkapp.k8s.ingressCheck"],n=["metric"],g=["hits"],o=function(h,x){if(i.default.indexOf(W,h)!==-1)return _.default.createElement(a.default,R({refresh:!1},x));var l=i.default.split(h,".",3);return l[0]===t&&i.default.indexOf(n,l[1])!==-1?_.default.createElement(a.default,R({refresh:!0},x)):l[0]===t&&i.default.indexOf(g,l[1])!==-1?_.default.createElement(r.default,R({refresh:!0},x)):null};E.getPlugin=o})},53369:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(15286),e(93080),e(8583),e(30553),e(27378),e(66697),e(74590),e(14798)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f,T,A,S,E,p,r,a){"use strict";var _=e(67971);y(m,"__esModule",{value:!0}),m.default=void 0,f=_(f),T=_(T),A=_(A),S=_(S),E=_(E),p=_(p),r=_(r),a=_(a);var i=S.default.Group,B=A.default.Item,u={labelCol:{fixedSpan:5},wrapperCol:{span:19}},R={labelCol:{fixedSpan:0},wrapperCol:{span:24}};function t(n){function g(F,Q){var z=n.onFormChange,N={};N[Q]=F,z&&z(N)}var o=n.specialDom,s=n.data,h=n.value,x=s.experimentName,l=s.startTime;return E.default.createElement(A.default,(0,T.default)({},u,{size:"small"}),E.default.createElement(B,{label:a.default.t("Drill name").toString()},E.default.createElement("p",null,x||"")),E.default.createElement(B,{label:a.default.t("Execution time").toString()},E.default.createElement("p",null,l&&(0,r.default)(l))),E.default.createElement(B,{label:a.default.t("In conclusion").toString()},E.default.createElement(i,{value:h&&h.expectationStatus,onChange:function(Q){return g(Q,"expectationStatus")}},E.default.createElement(S.default,{value:1},E.default.createElement(p.default,null,"In line with expectations")),E.default.createElement(S.default,{value:0},E.default.createElement(p.default,null,"Not as expected")))),E.default.createElement(B,{label:a.default.t("Affect normal business").toString()},E.default.createElement(i,{value:h&&h.businessStatus,onChange:function(Q){return g(Q,"businessStatus")}},E.default.createElement(S.default,{value:1},E.default.createElement(p.default,null,"Influence")),E.default.createElement(S.default,{value:0},E.default.createElement(p.default,null,"Does not affect")))),o&&E.default.createElement(B,(0,T.default)({},R,{size:"medium"}),o()),E.default.createElement(B,{label:a.default.t("Illustrate").toString()},E.default.createElement(f.default.TextArea,{placeholder:a.default.t("Please enter business description information").toString(),onChange:function(Q){return g(Q,"memo")},maxLength:200,showLimitHint:!0})))}var W=t;m.default=W})},26650:function(w,C,e){var d,I,c,y=e(67394),m=e(83452),f=e(95315),T=e(23587),A=e(63774),S=e(92937);(function(E,p){if(!0)!(I=[C,e(93080),e(28757),e(15286),e(92243),e(57379),e(30553),e(8583),e(27378),e(98784)],d=p,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(E,p,r,a,_,i,B,u,R,t){"use strict";var W=e(67971);y(E,"__esModule",{value:!0}),E.default=void 0,p=W(p),r=W(r),a=W(a),_=W(_),i=W(i),B=W(B),u=W(u),R=W(R),t=W(t);function n(F,Q){var z=m(F);if(f){var N=f(F);Q&&(N=N.filter(function(b){return T(F,b).enumerable})),z.push.apply(z,N)}return z}function g(F){for(var Q=1;Q<arguments.length;Q++){var z=arguments[Q]!=null?arguments[Q]:{};Q%2?n(Object(z),!0).forEach(function(N){(0,i.default)(F,N,z[N])}):A?S(F,A(z)):n(Object(z)).forEach(function(N){y(F,N,T(z,N))})}return F}var o=u.default.Item,s=B.default.Group,h={labelCol:{fixedSpan:6},wrapperCol:{span:18}};function x(F){function Q(V,O){var j=F.onSpecialDomChange,fe=F.data,ue=g(g({},O),{},{value:V}),te=t.default.get(fe,"extra.options",[]),U=[];t.default.isEmpty(te)||te.forEach(function(K){K.key===O.key&&(K=ue),U.push(K)});var M=g(g({},fe),{},{extra:g(g({},fe.extra),{},{options:U})});j&&j(M)}function z(V){var O=V.description,j=V.format,fe=V.value,ue=fe===void 0?"":fe,te=t.default.get(j,"type",""),U=j||{},M=U.required,K=M===void 0?!1:M,X=U.options,$=X===void 0?[]:X,le=U.writable,ae=le===void 0?!1:le,Ae=U.defaultValue,ve=Ae===void 0?"":Ae,Ee=U.placeholder,ie=Ee===void 0?"":Ee;if(te==="text")return R.default.createElement(o,{label:O},R.default.createElement(_.default,{trigger:R.default.createElement("p",{style:{overflow:"hidden",textOverflow:"ellipsis"}},ue),closable:!1},R.default.createElement("div",null,ue)));if(te==="input")return R.default.createElement(o,{label:O,required:K},R.default.createElement(a.default,{value:ue,placeholder:ie,disabled:!ae,defaultValue:ve,onChange:function(de){return Q(de,V)}}));if(te==="radio")return R.default.createElement(o,{label:O,required:K},R.default.createElement(s,{value:ue,dataSource:$,onChange:function(de){return Q(de,V)}}));if(te==="select")return R.default.createElement(o,(0,p.default)({label:O},h,{required:K}),R.default.createElement(r.default,{value:ue,style:{width:"100%"},onChange:function(de){return Q(de,V)},dataSource:$}))}var N=F.data,b=t.default.get(N,"extra.options",[]);return R.default.createElement(u.default,(0,p.default)({},h,{size:"small"}),!t.default.isEmpty(b)&&b.map(function(V){return z(V)}))}var l=x;E.default=l})},74590:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(61320)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f){"use strict";var T=e(67971);y(m,"__esModule",{value:!0}),m.default=void 0,f=T(f);var A=function(p){return p?(0,f.default)(p).format("YYYY-MM-DD HH:mm:ss"):""},S=A;m.default=S})},68055:function(w,C,e){var d,I,c,y=e(67394);(function(m,f){if(!0)!(I=[C,e(46454),e(17014),e(57379),e(13026)],d=f,c=typeof d=="function"?d.apply(C,I):d,c!==void 0&&(w.exports=c));else var T})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(m,f,T,A,S){"use strict";var E=e(67971);y(m,"__esModule",{value:!0}),m.default=void 0,f=E(f),T=E(T),A=E(A);var p={en:{Timeline:{expand:"Expand",fold:"Fold"},Balloon:{close:"Close"},Card:{expand:"Expand",fold:"Fold"},Dialog:{close:"Close",ok:"Confirm",cancel:"Cancel"},Drawer:{close:"Close"},Message:{closeAriaLabel:"Close"},Pagination:{prev:"Prev",next:"Next",goTo:"Go To",page:"Page",go:"Go",total:"Page {current} of {total} pages.",labelPrev:"Prev page, current page {current}",labelNext:"Next page, current page {current}",inputAriaLabel:"Please enter the page to jump to",selectAriaLabel:"Please select page size",pageSize:"Page Size:"},Input:{clear:"Clear"},List:{empty:"No Data"},Select:{selectPlaceholder:"Please select",autoCompletePlaceholder:"Please enter",notFoundContent:"No Options",maxTagPlaceholder:"{selected}/{total} items have been selected.",selectAll:"Select All"},Table:{empty:"No Data",ok:"Confirm",reset:"Reset",asc:"Asc",desc:"Desc",expanded:"Expanded",folded:"Folded",filter:"Filter",selectAll:"Select All"},Upload:{card:{cancel:"Cancel",delete:"Delete"},upload:{delete:"Delete"}},Search:{buttonText:"Search"},Tag:{delete:"Delete"},Switch:{on:"On",off:"Off"},Tab:{closeAriaLabel:"Close"},Dropdown:{empty:"No Data"},Radio:{empty:"No Data"}},zh:{Timeline:{expand:"\u5C55\u5F00",fold:"\u6536\u8D77"},Balloon:{close:"\u5173\u95ED"},Card:{expand:"\u5C55\u5F00",fold:"\u6536\u8D77"},Dialog:{close:"\u5173\u95ED",ok:"\u786E\u8BA4",cancel:"\u53D6\u6D88"},Drawer:{close:"\u5173\u95ED"},Message:{closeAriaLabel:"\u5173\u95ED\u6807\u7B7E"},Pagination:{prev:"\u524D\u4E00\u9875",next:"\u4E0B\u4E00\u9875",goTo:"\u53BB\u5F80",page:"\u5206\u9875",go:"\u53BB",total:"Page {current} of {total} pages.",labelPrev:"\u524D\u4E00\u9875, \u5F53\u524D\u9875 {current}",labelNext:"\u4E0B\u4E00\u9875, \u5F53\u524D\u9875 {current}",inputAriaLabel:"\u8BF7\u8F93\u5165\u8981\u8DF3\u8F6C\u5230\u7684\u9875\u9762",selectAriaLabel:"\u8BF7\u9009\u62E9\u9875\u9762\u5C55\u793A\u7684\u6570\u91CF",pageSize:"\u6BCF\u9875\u663E\u793A\u591A\u5C11\u6761:"},Input:{clear:"\u6E05\u7A7A"},List:{empty:"\u6CA1\u6709\u6570\u636E"},Select:{selectPlaceholder:"\u8BF7\u9009\u62E9",autoCompletePlaceholder:"\u8BF7\u8F93\u5165",notFoundContent:"\u6CA1\u6709\u4E0B\u62C9\u9879",maxTagPlaceholder:"{selected}/{total} \u6761\u76EE\u5DF2\u9009\u62E9.",selectAll:"\u5168\u9009"},Table:{empty:"\u6CA1\u6709\u6570\u636E",ok:"\u786E\u8BA4",reset:"\u91CD\u7F6E",asc:"\u751F\u5E8F",desc:"\u964D\u5E8F",expanded:"\u5C55\u5F00",folded:"\u6536\u8D77",filter:"\u8FC7\u6EE4",selectAll:"\u5168\u9009"},Upload:{card:{cancel:"\u53D6\u6D88",delete:"\u5220\u9664"},upload:{delete:"\u5220\u9664"}},Search:{buttonText:"\u641C\u7D22"},Tag:{delete:"\u5220\u9664"},Switch:{on:"\u6253\u5F00",off:"\u5173\u95ED"},Tab:{closeAriaLabel:"\u5173\u95ED"},Dropdown:{empty:"\u6CA1\u6709\u6570\u636E"},Radio:{empty:"\u6CA1\u6709\u6570\u636E"}}},r=function(){function _(){(0,f.default)(this,_),(0,A.default)(this,"local",void 0)}return(0,T.default)(_,[{key:"setLocal",value:function(B){this.local=B}},{key:"getLocal",value:function(){var B=this;return function(){var u,R=(0,S.getLanguage)();return(u=B.local[R])!==null&&u!==void 0?u:B.local.en}}}],[{key:"getInstance",value:function(){return this.instance=this.instance||new _,this.instance}}]),_}();(0,A.default)(r,"instance",null),r.getInstance().setLocal(p);var a=r.getInstance().getLocal();m.default=a})},4275:(w,C,e)=>{e(70285),e(99572),e(74114),e(89324),e(84971),e(46042),e(24899),w.exports=e(47208).Set},89324:(w,C,e)=>{"use strict";var d=e(29071),I=e(41081),c="Set";w.exports=e(70770)(c,function(y){return function(){return y(this,arguments.length>0?arguments[0]:void 0)}},{add:function(m){return d.def(I(this,c),m=m===0?0:m,m)}},d)},24899:(w,C,e)=>{e(14689)("Set")},46042:(w,C,e)=>{e(7883)("Set")},84971:(w,C,e)=>{var d=e(98310);d(d.P+d.R,"Set",{toJSON:e(89518)("Set")})},74681:(w,C,e)=>{"use strict";e.d(C,{Z:()=>f});var d=e(60994),I=e.n(d),c=e(93476),y=e.n(c),m=y()(I());m.push([w.id,`.index__chartAction__8IhXJ {
  display: flex;
  justify-content: space-between;
  color: #818181;
  width: 95%;
}

  .index__chartAction__8IhXJ .index__iconCon__2Cviq i::before{
      width: 14px !important;
      height: 14px !important;
      font-size: 14px !important;
      cursor: pointer;
    }

  .index__chartAction__8IhXJ .index__iconCon__2Cviq:nth-child(1) {
      margin-right: 10px;
    }

  .index__chartAction__8IhXJ .index__changeBig__aHSXw {
    transform: rotate(45deg);
    -webkit-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -o-transform: rotate(45deg);
  }

.index__iconConBig__D2u0b {
  cursor: pointer;
}

.index__iconConBig__D2u0b i::before{
    width: 20px !important;
    height: 20px !important;
    font-size: 20px !important;
    cursor: pointer;
  }

`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentTask/Chart/index.css"],names:[],mappings:"AAAA;EACE,aAAa;EACb,8BAA8B;EAC9B,cAAc;EACd,UAAU;AAsBZ;;EAnBI;MACE,sBAAsB;MACtB,uBAAuB;MACvB,0BAA0B;MAC1B,eAAe;IACjB;;EAEA;MACE,kBAAkB;IACpB;;EAGF;IACE,wBAAwB;IACxB,gCAAgC;IAChC,6BAA6B;IAC7B,4BAA4B;IAC5B,2BAA2B;EAC7B;;AAGF;EACE,eAAe;AAQjB;;AANE;IACE,sBAAsB;IACtB,uBAAuB;IACvB,0BAA0B;IAC1B,eAAe;EACjB",sourcesContent:[`.chartAction {
  display: flex;
  justify-content: space-between;
  color: #818181;
  width: 95%;

  .iconCon {
    i::before{
      width: 14px !important;
      height: 14px !important;
      font-size: 14px !important;
      cursor: pointer;
    }

    &:nth-child(1) {
      margin-right: 10px;
    }
  }

  .changeBig {
    transform: rotate(45deg);
    -webkit-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -o-transform: rotate(45deg);
  }
}

.iconConBig {
  cursor: pointer;
  
  i::before{
    width: 20px !important;
    height: 20px !important;
    font-size: 20px !important;
    cursor: pointer;
  }
}

`],sourceRoot:""}]),m.locals={chartAction:"index__chartAction__8IhXJ",iconCon:"index__iconCon__2Cviq",changeBig:"index__changeBig__aHSXw",iconConBig:"index__iconConBig__D2u0b"};const f=m},10981:(w,C,e)=>{"use strict";e.d(C,{Z:()=>f});var d=e(60994),I=e.n(d),c=e(93476),y=e.n(c),m=y()(I());m.push([w.id,`.index__basicContent__YKDB4 {
  width: 100%;
}

  .index__basicContent__YKDB4 .index__title__XURSf {
    height: 32px;
    width: 56px;
    font-family: PingFangSC-Medium;
    font-size: 14px;
    color: #333333;
    line-height: 32px;
    margin-bottom: 8px;
  }

  .index__basicContent__YKDB4 .index__basicItem__1wup1 {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    font-size: 12px;
    color: #333333;
    line-height: 20px;
    margin-bottom: 8px;
  }

  .index__basicContent__YKDB4 .index__basicItem__1wup1 .index__label__J2\\+aX {
      width: 10%;
    }

  .index__basicContent__YKDB4 .index__basicItem__1wup1 .index__value__IXGLR {
      width: 70%;
      white-space: pre-wrap;
    }`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentTask/TaskBasic/index.css"],names:[],mappings:"AAAA;EACE,WAAW;AA8Bb;;EA5BE;IACE,YAAY;IACZ,WAAW;IACX,8BAA8B;IAC9B,eAAe;IACf,cAAc;IACd,iBAAiB;IACjB,kBAAkB;EACpB;;EAEA;IACE,WAAW;IACX,aAAa;IACb,2BAA2B;IAC3B,eAAe;IACf,cAAc;IACd,iBAAiB;IACjB,kBAAkB;EAUpB;;EARE;MACE,UAAU;IACZ;;EAEA;MACE,UAAU;MACV,qBAAqB;IACvB",sourcesContent:[`.basicContent {
  width: 100%;

  .title {
    height: 32px;
    width: 56px;
    font-family: PingFangSC-Medium;
    font-size: 14px;
    color: #333333;
    line-height: 32px;
    margin-bottom: 8px;
  }

  .basicItem {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    font-size: 12px;
    color: #333333;
    line-height: 20px;
    margin-bottom: 8px;

    .label {
      width: 10%;
    }

    .value {
      width: 70%;
      white-space: pre-wrap;
    }
  }
}`],sourceRoot:""}]),m.locals={basicContent:"index__basicContent__YKDB4",title:"index__title__XURSf",basicItem:"index__basicItem__1wup1",label:"index__label__J2+aX",value:"index__value__IXGLR"};const f=m},72430:(w,C,e)=>{"use strict";e.d(C,{Z:()=>f});var d=e(60994),I=e.n(d),c=e(93476),y=e.n(c),m=y()(I());m.push([w.id,`.index__infoContent__Q4FGE {
  width: 100%;
  position: relative;

}

  .index__infoContent__Q4FGE .index__machine__Bp550{
    padding-top: 10px;
  }

  .index__infoContent__Q4FGE .index__machine__Bp550 .index__success__ZXihv {
      color: #1E8E3E;
    }

  .index__infoContent__Q4FGE .index__machine__Bp550 .index__faile__53eCq {
      color: #D93026;
    }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI {
    margin-top: 10px;
    width: 100%;
    min-height: 200px;
    background: #FAFAFA 100%;
    padding: 14px;
  }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v {
      margin-bottom: 6px;
      display: flex;
      justify-content: space-between;
    }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__iconAndWord__ewQIe {
        display: flex;
        justify-content: flex-start;
        align-items: center;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__titleIcon__ACPiF {
        margin-right: 6px;
        color: #0070CC;
        width: 15px;
        height: 16px;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__titleIcon__ACPiF::before{
          width: 15px !important;
          font-size: 15px !important;
        }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__title__ecWyP {
        font-family: PingFangSC-Medium;
        font-size: 14px;
        color: #333333;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__tipsIcon__nzPmQ {
        color: #5A5A5A;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__tipsIcon__nzPmQ::before{
          font-size: 12px !important;
        }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__detailTop__XX76v .index__tips__9xrWE {
        font-family: PingFangSC-Regular;
        font-size: 12px;
        color: #888888;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__ipsContent__1gpCq .index__ipsSearch__rQmEA {
        width: 100%;
        margin-bottom: 10px;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__ipsContent__1gpCq .index__ips__FEy0Z {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        margin-bottom: 12px;
        margin-right: -8px;
      }

  .index__infoContent__Q4FGE .index__machineDetail__j0jKI .index__content__\\+mRwY {
      width: 14px;
      height: 14px;
      margin-right: 8px;
      margin-bottom: 8px;
      cursor: pointer;
    }

  .index__infoContent__Q4FGE .index__userCheckState__ppCo1 {
    display: flex;
    justify-content: flex-start;
    margin-top: 14px;
  }

  .index__infoContent__Q4FGE .index__userCheckState__ppCo1 .index__checkStateLabel__4Q2pt {
      margin-right: 16px;
    }

  .index__infoContent__Q4FGE .index__userCheckState__ppCo1 .index__checkPass__P4aqM {
      color: #7ec12d;
    }

  .index__infoContent__Q4FGE .index__userCheckState__ppCo1 .index__checkFailed__3R7Ie {
      color: #ff4f4c;
    }

.index__logTabs__P\\+eyc .next-tabs-scrollable .next-tabs-nav-scroll {
    padding-right: 36px;
  }

.index__infoDialog__Fot9s {
  height: calc(100% - 60px);
}

.index__infoDialog__Fot9s .next-dialog-close {
    z-index: 2!important;
  }

.index__infoDialog__Fot9s .next-dialog-body {
  /* height: calc(100% - 180px);
  overflow-x: hidden; */
  padding: 0 24px;
}

.index__infoContainer__qZxWp .index__detailInfo__TebBU {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    padding: 8px;
    border: 1px solid #f5f5f5;
    word-break: break-word;
    font-size: 12px;
    display: flex;
    flex-wrap: wrap;
  }

.index__infoContainer__qZxWp .index__detailInfo__TebBU > div {
      width: 50%;
      height: 26px;
      line-height: 26px;
    }

.index__infoContainer__qZxWp .index__detailInfo__TebBU > div span {
        width: 70px;
        display: inline-block;
      }

.index__infoContainer__qZxWp .index__detailInfo__TebBU > div .index__link__hjbCg {
        color: #0064C8;
        cursor: pointer;
      }

.index__infoContainer__qZxWp .index__detailInfo__TebBU > div .index__link__hjbCg:hover {
          color: #015396;
        }

.index__infoContainer__qZxWp .index__infoTitle__NryF\\+ {
    font-size: 16px;
    font-weight: bold;
  }

.index__infoContainer__qZxWp .index__infoContent__Q4FGE {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    padding: 8px;
    border: 1px solid #f5f5f5;
    word-break: break-word;
  }

.index__infoContainer__qZxWp .index__infoContent__Q4FGE pre {
      white-space: pre-wrap !important;
      word-wrap: break-word !important;
      font-size: 13px !important;
    }

.index__infoContainer__qZxWp .index__msgs__GWH68 {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    border: 1px solid #ddd;
    white-space: pre-wrap !important;
    word-wrap: break-word !important;
    overflow: auto;
    font-size: 13px !important;
  }

.index__infoContainer__qZxWp .index__msgs__GWH68 > div {
      padding: 8px;
      border-bottom: 1px solid #ddd;
    }

.index__infoContainer__qZxWp .index__msgs__GWH68 > div > strong {
        margin-right: 4px;
      }

.index__infoContainer__qZxWp .index__msgs__GWH68 > div:last-child {
        border: none;
      }

.index__actionCon__wVZ\\+u {
  position: absolute;
  top: 6px;
  right: 0px;
  z-index: 99;
}

.index__actionCon__wVZ\\+u .index__action__5sEwe {
    cursor: pointer;
    font-size: 12px;
    color: #0070CC;
  }

.index__actionCon__wVZ\\+u .index__noParamsStyle__iRkuO {
    cursor: not-allowed;
    color: #c1c1c1;
  }

.index__actionCon__wVZ\\+u .index__actionLine__6wY9b {
    font-size: 12px;
    color: #c9c9c9;
    margin: 0 11px;
  }

.index__logShowContent__SQy0s {
  width: 100%;
  height: 100%;
  background: #f7f7f7;
  overflow: auto;
  border: 1px solid #f5f5f5;
  font-size: 13px !important;
  border: 1px solid #ddd;
}

.index__logShowContent__SQy0s > .index__logShow__LZLZq {
    white-space: pre-wrap !important;
    word-wrap: break-word !important;
    padding: 8px;
    border-bottom: 1px solid #ddd;
  }

.index__logShowContent__SQy0s > .index__logShow__LZLZq:last-child {
      border: none;
    }

.index__logShowContent__SQy0s > .index__logShow__LZLZq > strong {
      margin-right: 4px;
    }

.index__logShowContent__SQy0s > .index__logShow__LZLZq.index__debug__tgzzQ {
      color: #999999;
    }

.index__logShowContent__SQy0s > .index__logShow__LZLZq.index__info__ey-Fc {
      color: #1D4046;
    }

.index__logShowContent__SQy0s > .index__logShow__LZLZq.index__warn__14hxh {
      color: #FF970D;
    }

.index__logShowContent__SQy0s > .index__logShow__LZLZq.index__error__mopUN {
      color: #FF4F4C;
    }



.index__loadingStyle__MGArZ {
  padding: 40%;
  width: 800px;
  text-align: center;
}
.index__pourLogs__1bKtN {
  position: relative;
  height: calc(100vh - 150px);
  overflow: hidden;
}
.index__pourLogs__1bKtN .index__logHeader__9Q9xj {
    padding: 6px;
    background: #eaeaea;
    border-radius: 4px;
  }
.index__pourLogs__1bKtN .index__logContent__LzEvd {
    width: 100%;
    position: absolute;
    top: 34px;
    bottom: 40px;
    overflow: auto;
  }
.index__pourLogs__1bKtN .index__logContent__LzEvd > div {
      border-bottom: 1px solid #ddd;
      padding: 6px;
      /* &:last-child {
        border: none;
      } */
    }
.index__pourLogs__1bKtN .next-pagination {
    position: absolute;
    bottom: 0px;
    right: 0;
  }
.index__pourLogs__1bKtN .index__logTitle__pOvkx {
    color: #333;
  }
.index__pourLogs__1bKtN .index__logDesp__eBSlN {
    padding: 4px 0;
    border: 0;
    font-size: 12px;
    color: #666;
    line-height: 20px;
  }
.index__pourLogs__1bKtN .index__pullRight__9cSzS {
    float: right;
    font-size: 12px;
  }
`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentTask/TaskInfo/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,kBAAkB;;AA6GpB;;EA3GE;IACE,iBAAiB;EASnB;;EAPE;MACE,cAAc;IAChB;;EAEA;MACE,cAAc;IAChB;;EAGF;IACE,gBAAgB;IAChB,WAAW;IACX,iBAAiB;IACjB,wBAAwB;IACxB,aAAa;EAsEf;;EApEE;MACE,kBAAkB;MAClB,aAAa;MACb,8BAA8B;IAuChC;;EArCE;QACE,aAAa;QACb,2BAA2B;QAC3B,mBAAmB;MACrB;;EAEA;QACE,iBAAiB;QACjB,cAAc;QACd,WAAW;QACX,YAAY;MAMd;;EAJE;UACE,sBAAsB;UACtB,0BAA0B;QAC5B;;EAGF;QACE,8BAA8B;QAC9B,eAAe;QACf,cAAc;MAChB;;EAEA;QACE,cAAc;MAKhB;;EAHE;UACE,0BAA0B;QAC5B;;EAGF;QACE,+BAA+B;QAC/B,eAAe;QACf,cAAc;MAChB;;EAIA;QACE,WAAW;QACX,mBAAmB;MACrB;;EAEA;QACE,aAAa;QACb,2BAA2B;QAC3B,eAAe;QACf,mBAAmB;QACnB,kBAAkB;MACpB;;EAKF;MACE,WAAW;MACX,YAAY;MACZ,iBAAiB;MACjB,kBAAkB;MAClB,eAAe;IACjB;;EAGF;IACE,aAAa;IACb,2BAA2B;IAC3B,gBAAgB;EAalB;;EAXE;MACE,kBAAkB;IACpB;;EAEA;MACE,cAAc;IAChB;;EAEA;MACE,cAAc;IAChB;;AAMF;IACE,mBAAmB;EACrB;;AAGF;EACE,yBAAyB;AAI3B;;AAHE;IACE,oBAAoB;EACtB;;AAGF;EACE;uBACqB;EACrB,eAAe;AACjB;;AAIE;IACE,cAAc;IACd,WAAW;IACX,mBAAmB;IACnB,YAAY;IACZ,yBAAyB;IACzB,sBAAsB;IACtB,eAAe;IACf,aAAa;IACb,eAAe;EAiBjB;;AAhBE;MACE,UAAU;MACV,YAAY;MACZ,iBAAiB;IAYnB;;AAXE;QACE,WAAW;QACX,qBAAqB;MACvB;;AACA;QACE,cAAc;QACd,eAAe;MAIjB;;AAHE;UACE,cAAc;QAChB;;AAKN;IACE,eAAe;IACf,iBAAiB;EACnB;;AAEA;IACE,cAAc;IACd,WAAW;IACX,mBAAmB;IACnB,YAAY;IACZ,yBAAyB;IACzB,sBAAsB;EAOxB;;AALE;MACE,gCAAgC;MAChC,gCAAgC;MAChC,0BAA0B;IAC5B;;AAEF;IACE,cAAc;IACd,WAAW;IACX,mBAAmB;IACnB,sBAAsB;IACtB,gCAAgC;IAChC,gCAAgC;IAChC,cAAc;IACd,0BAA0B;EAW5B;;AAVE;MACE,YAAY;MACZ,6BAA6B;IAO/B;;AANE;QACE,iBAAiB;MACnB;;AACA;QACE,YAAY;MACd;;AAKN;EACE,kBAAkB;EAClB,QAAQ;EACR,UAAU;EACV,WAAW;AAkBb;;AAhBE;IACE,eAAe;IACf,eAAe;IACf,cAAc;EAChB;;AAEA;IACE,mBAAmB;IACnB,cAAc;EAChB;;AAEA;IACE,eAAe;IACf,cAAc;IACd,cAAc;EAChB;;AAGF;EACE,WAAW;EACX,YAAY;EACZ,mBAAmB;EACnB,cAAc;EACd,yBAAyB;EACzB,0BAA0B;EAC1B,sBAAsB;AAgCxB;;AA9BE;IACE,gCAAgC;IAChC,gCAAgC;IAChC,YAAY;IACZ,6BAA6B;EAyB/B;;AAvBE;MACE,YAAY;IACd;;AAEA;MACE,iBAAiB;IACnB;;AAEA;MACE,cAAc;IAChB;;AAEA;MACE,cAAc;IAChB;;AAEA;MACE,cAAc;IAChB;;AAEA;MACE,cAAc;IAChB;;;;AAMJ;EACE,YAAY;EACZ,YAAY;EACZ,kBAAkB;AACpB;AACA;EACE,kBAAkB;EAClB,2BAA2B;EAC3B,gBAAgB;AAyClB;AAxCE;IACE,YAAY;IACZ,mBAAmB;IACnB,kBAAkB;EACpB;AACA;IACE,WAAW;IACX,kBAAkB;IAClB,SAAS;IACT,YAAY;IACZ,cAAc;EAShB;AAPE;MACE,6BAA6B;MAC7B,YAAY;MACZ;;SAEG;IACL;AAEF;IACE,kBAAkB;IAClB,WAAW;IACX,QAAQ;EACV;AACA;IACE,WAAW;EACb;AACA;IACE,cAAc;IACd,SAAS;IACT,eAAe;IACf,WAAW;IACX,iBAAiB;EACnB;AAEA;IACE,YAAY;IACZ,eAAe;EACjB",sourcesContent:[`.infoContent {
  width: 100%;
  position: relative;

  .machine{
    padding-top: 10px;

    .success {
      color: #1E8E3E;
    }

    .faile {
      color: #D93026;
    }
  }

  .machineDetail {
    margin-top: 10px;
    width: 100%;
    min-height: 200px;
    background: #FAFAFA 100%;
    padding: 14px;

    .detailTop {
      margin-bottom: 6px;
      display: flex;
      justify-content: space-between;

      .iconAndWord {
        display: flex;
        justify-content: flex-start;
        align-items: center;
      }

      .titleIcon {
        margin-right: 6px;
        color: #0070CC;
        width: 15px;
        height: 16px;

        &::before{
          width: 15px !important;
          font-size: 15px !important;
        }
      }

      .title {
        font-family: PingFangSC-Medium;
        font-size: 14px;
        color: #333333;
      }

      .tipsIcon {
        color: #5A5A5A;

        &::before{
          font-size: 12px !important;
        }
      }

      .tips {
        font-family: PingFangSC-Regular;
        font-size: 12px;
        color: #888888;
      }
    }
    .ipsContent {

      .ipsSearch {
        width: 100%;
        margin-bottom: 10px;
      }

      .ips {
        display: flex;
        justify-content: flex-start;
        flex-wrap: wrap;
        margin-bottom: 12px;
        margin-right: -8px;
      }
    }

   

    .content {
      width: 14px;
      height: 14px;
      margin-right: 8px;
      margin-bottom: 8px;
      cursor: pointer;
    }
  }

  .userCheckState {
    display: flex;
    justify-content: flex-start;
    margin-top: 14px;

    .checkStateLabel {
      margin-right: 16px;
    }

    .checkPass {
      color: #7ec12d;
    }

    .checkFailed {
      color: #ff4f4c;
    }
  }

}

.logTabs {
  :global(.next-tabs-scrollable .next-tabs-nav-scroll) {
    padding-right: 36px;
  }
}

.infoDialog {
  height: calc(100% - 60px);
  :global(.next-dialog-close) {
    z-index: 2!important;
  }
}

.infoDialog :global(.next-dialog-body) {
  /* height: calc(100% - 180px);
  overflow-x: hidden; */
  padding: 0 24px;
}

.infoContainer {

  .detailInfo {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    padding: 8px;
    border: 1px solid #f5f5f5;
    word-break: break-word;
    font-size: 12px;
    display: flex;
    flex-wrap: wrap;
    > div {
      width: 50%;
      height: 26px;
      line-height: 26px;
      span {
        width: 70px;
        display: inline-block;
      }
      .link {
        color: #0064C8;
        cursor: pointer;
        &:hover {
          color: #015396;
        }
      }
    }
  }

  .infoTitle {
    font-size: 16px;
    font-weight: bold;
  }

  .infoContent {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    padding: 8px;
    border: 1px solid #f5f5f5;
    word-break: break-word;

    pre {
      white-space: pre-wrap !important;
      word-wrap: break-word !important;
      font-size: 13px !important;
    }
  }
  .msgs {
    margin: 12px 0;
    width: 100%;
    background: #f7f7f7;
    border: 1px solid #ddd;
    white-space: pre-wrap !important;
    word-wrap: break-word !important;
    overflow: auto;
    font-size: 13px !important;
    > div {
      padding: 8px;
      border-bottom: 1px solid #ddd;
      > strong {
        margin-right: 4px;
      }
      &:last-child {
        border: none;
      }
    }
  }
}

.actionCon {
  position: absolute;
  top: 6px;
  right: 0px;
  z-index: 99;
  
  .action {
    cursor: pointer;
    font-size: 12px;
    color: #0070CC;
  }

  .noParamsStyle {
    cursor: not-allowed;
    color: #c1c1c1;
  }

  .actionLine {
    font-size: 12px;
    color: #c9c9c9;
    margin: 0 11px;
  }
}

.logShowContent {
  width: 100%;
  height: 100%;
  background: #f7f7f7;
  overflow: auto;
  border: 1px solid #f5f5f5;
  font-size: 13px !important;
  border: 1px solid #ddd;

  > .logShow {
    white-space: pre-wrap !important;
    word-wrap: break-word !important;
    padding: 8px;
    border-bottom: 1px solid #ddd;

    &:last-child {
      border: none;
    }

    > strong {
      margin-right: 4px;
    }
  
    &.debug {
      color: #999999;
    }
  
    &.info {
      color: #1D4046;
    }
  
    &.warn {
      color: #FF970D;
    }
  
    &.error {
      color: #FF4F4C;
    }
  }
}



.loadingStyle {
  padding: 40%;
  width: 800px;
  text-align: center;
}
.pourLogs {
  position: relative;
  height: calc(100vh - 150px);
  overflow: hidden;
  .logHeader {
    padding: 6px;
    background: #eaeaea;
    border-radius: 4px;
  }
  .logContent {
    width: 100%;
    position: absolute;
    top: 34px;
    bottom: 40px;
    overflow: auto;
    
    > div {
      border-bottom: 1px solid #ddd;
      padding: 6px;
      /* &:last-child {
        border: none;
      } */
    }
  }
  :global(.next-pagination) {
    position: absolute;
    bottom: 0px;
    right: 0;
  }
  .logTitle {
    color: #333;
  }
  .logDesp {
    padding: 4px 0;
    border: 0;
    font-size: 12px;
    color: #666;
    line-height: 20px;
  }
  
  .pullRight {
    float: right;
    font-size: 12px;
  }
}
`],sourceRoot:""}]),m.locals={infoContent:"index__infoContent__Q4FGE",machine:"index__machine__Bp550",success:"index__success__ZXihv",faile:"index__faile__53eCq",machineDetail:"index__machineDetail__j0jKI",detailTop:"index__detailTop__XX76v",iconAndWord:"index__iconAndWord__ewQIe",titleIcon:"index__titleIcon__ACPiF",title:"index__title__ecWyP",tipsIcon:"index__tipsIcon__nzPmQ",tips:"index__tips__9xrWE",ipsContent:"index__ipsContent__1gpCq",ipsSearch:"index__ipsSearch__rQmEA",ips:"index__ips__FEy0Z",content:"index__content__+mRwY",userCheckState:"index__userCheckState__ppCo1",checkStateLabel:"index__checkStateLabel__4Q2pt",checkPass:"index__checkPass__P4aqM",checkFailed:"index__checkFailed__3R7Ie",logTabs:"index__logTabs__P+eyc",infoDialog:"index__infoDialog__Fot9s",infoContainer:"index__infoContainer__qZxWp",detailInfo:"index__detailInfo__TebBU",link:"index__link__hjbCg",infoTitle:"index__infoTitle__NryF+",msgs:"index__msgs__GWH68",actionCon:"index__actionCon__wVZ+u",action:"index__action__5sEwe",noParamsStyle:"index__noParamsStyle__iRkuO",actionLine:"index__actionLine__6wY9b",logShowContent:"index__logShowContent__SQy0s",logShow:"index__logShow__LZLZq",debug:"index__debug__tgzzQ",info:"index__info__ey-Fc",warn:"index__warn__14hxh",error:"index__error__mopUN",loadingStyle:"index__loadingStyle__MGArZ",pourLogs:"index__pourLogs__1bKtN",logHeader:"index__logHeader__9Q9xj",logContent:"index__logContent__LzEvd",logTitle:"index__logTitle__pOvkx",logDesp:"index__logDesp__eBSlN",pullRight:"index__pullRight__9cSzS"};const f=m},74682:(w,C,e)=>{"use strict";e.d(C,{Z:()=>f});var d=e(60994),I=e.n(d),c=e(93476),y=e.n(c),m=y()(I());m.push([w.id,`.index__warper__18o4L {
  padding: 0 0 40px 0;
}

  .index__warper__18o4L .index__charts__ta2QJ {
    margin-top: 16px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
  }

  .index__warper__18o4L .index__charts__ta2QJ > div {
      padding: 6px;
    }

  .index__warper__18o4L .index__stableCharts__vS9OL {
    padding-bottom: 16px;
    /* box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.13); */
    margin-bottom: 12px;
    border-bottom: 1px solid #ddd;
    
  }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln {
      /* height: 320px; */
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__stableIcon__e2BNZ {
        float: right;
        margin-right: 16px;
      }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__scTitle__hUGUT {
        font-size: 14px;
        color: #333;
        font-weight: bold;
        margin-bottom: 8px;
      }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sCharts__1Yjxf {
        width: 68%;
        padding-right: 10px;
        border-right: 1px solid #ddd;
      }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTableInfo__RkLuU {
        width: 30%;
        margin-left: 20px;
      }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw {
        width: 100%;
        border: 1px solid #ddd;
        
      }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw tr {
          height: 32px;
          line-height: 32px;
          border-bottom: 1px solid #ddd;
        }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw tr th, .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw tr td {
            padding: 2px 12px;
            border-right: 1px solid #ddd;
          }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw tr td {
            text-align: center;
          }

  .index__warper__18o4L .index__stableCharts__vS9OL .index__stableItem__Hueln .index__sTable__7wGPw th {
          background: #FAFAFA;
        }

  .index__warper__18o4L .index__line__KEsCJ {
    width: 100%;
    height: 1px;
    background: #dedede 100%;
    margin: 25px 0;
  }

  .index__warper__18o4L .index__strategies__9VerZ {
    width: 100%;
    margin-bottom: 32px;
  }

  .index__warper__18o4L .index__iconContent__06krr {
    position: relative;
  }

  .index__warper__18o4L .index__iconContent__06krr .index__circle__IaeAk {
      width: 28px;
      height: 28px;
      border-top: 2px solid #dde4ed;
      border-left: 2px solid #dde4ed;
      border-right: 2px solid #1e8e3e;
      border-bottom: 2px solid #1e8e3e;
      border-radius: 50%;
      -webkit-border-radius: 50%;
      -moz-border-radius: 50%;
      -ms-border-radius: 50%;
      -o-border-radius: 50%;
      animation: index__circle__IaeAk 2s linear infinite;
      -webkit-animation: index__circle__IaeAk 2s linear infinite;
    }

  .index__warper__18o4L .index__iconContent__06krr .index__circleStill__YOdeU {
      width: 28px;
      height: 28px;
      border: 2px solid #1e8e3e;
      border-radius: 50%;
    }

  .index__warper__18o4L .index__iconContent__06krr .index__svg__1mAMv {
      position: absolute;
      top: 6px;
      left: 7px;
    }

  .index__warper__18o4L .index__triggered__gBwsf {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .index__warper__18o4L .index__triggered__gBwsf .index__svgFail__-mG1q::before {
        font-size: 14px !important;
      }

  .index__warper__18o4L .index__triggered__gBwsf .index__svgFail__-mG1q {

      margin-right: 8px;
      color: #f15533;
}

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__timeRange__i8\\+aE {
      display: flex;
      width: 100%;
      justify-content: space-between;
      align-items: center;
    }

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__timeRange__i8\\+aE .index__start__k3EjF {
        margin-right: 30px;
      }

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__taskContent__Fhk-G {
      width: 100%;
      min-height: 300px;
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
    }

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__taskContent__Fhk-G .index__flows__ZUJPC {
        width: 70%;
        padding: 24px;
        background: #f7f7f7;
        margin-right: 24px;
        max-height: 300px;
        overflow-y: scroll;
      }

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__taskContent__Fhk-G .index__flows__ZUJPC .index__flowLoading__MZAg9 {
          width: 100%;
          height: 300px;
          display: flex;
          justify-content: center;
          align-items: center;
        }

  .index__warper__18o4L .index__taskDetail__j-M5\\+ .index__taskContent__Fhk-G .index__taskInfo__d8za1 {
        flex: 1;
      }

.index__titleTips__ks8bG {
  font-family: PingFangSC-Medium;
  font-size: 14px;
  color: #333333;
  line-height: 32px;
  margin-bottom: 8px;
}

.index__ruleDetail__Q4WNq {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #0070cc;
  cursor: pointer;
  line-height: 50px;
}

.index__showStrategy__1be-q {
  font-size: 14px;
  margin-top: 15px;
  margin-bottom: 15px;
}

.index__showTolerance__VutSA {
  background: #f7f7f7;
  padding: 8px;
  border: 1px solid #f5f5f5;
}

.index__tolerance__vZPQS {
  line-height: 20px;
}

.index__showRules__wL5Tq {
  background: #f7f7f7;
  padding: 8px;
  border: 1px solid #f5f5f5;
}

.index__buttonSecond__byH23 {
  margin: 0 8px !important;
}

.index__feedBack__YT3vN {
  width: 100%;
  padding: 20px 0;
}

.index__feedBack__YT3vN .index__feedTitle__NxZal {
    color: #333;
    font-size: 14px;
    line-height: 32px;
    margin-bottom: 8px;
    font-family: PingFangSC-Medium;
  }

.index__feedBack__YT3vN .index__feedItem__cKpEd {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    font-size: 12px;
    color: #555;
    line-height: 20px;
    margin-bottom: 8px;
  }

.index__feedBack__YT3vN .index__feedItem__cKpEd .index__label__tss0J {
      width: 7%;
      margin-right: 24px;
    }

.index__feedBack__YT3vN .index__feedItem__cKpEd .index__value__IphH3 {
      width: 90%;
    }

.index__dependenceDialog__VJA7C {
  width: 640px;
}

.index__dependenceDialog__VJA7C .next-dialog-body {
    max-height: 400px;
    overflow-y: scroll;
  }

.index__dependenceDialog__VJA7C .index__DialogFrom__SVrhU {
    width: 100%;
    height: 100%;
    /* overflow: scroll; */
  }

.index__specialDom__xMBKC {
  width: 100%;
  min-height: 57px;
  background: #f7f7f7;
  padding: 12px;
}

.index__normalBtn__7DIS1 {
  margin-left: 8px !important;
}

@-webkit-keyframes index__circle__IaeAk {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

@keyframes index__circle__IaeAk {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentTask/index.css","<no source>"],names:[],mappings:"AAAA;EACE,mBAAmB;AAwKrB;;EAtKE;IACE,gBAAgB;IAChB,aAAa;IACb,2BAA2B;IAC3B,eAAe;EAIjB;;EAHE;MACE,YAAY;IACd;;EAGF;IACE,oBAAoB;IACpB,iDAAiD;IACjD,mBAAmB;IACnB,6BAA6B;;EAiD/B;;EA/CE;MACE,mBAAmB;MACnB,aAAa;MACb,8BAA8B;MAC9B,mBAAmB;IAyCrB;;EAvCE;QACE,YAAY;QACZ,kBAAkB;MACpB;;EACA;QACE,eAAe;QACf,WAAW;QACX,iBAAiB;QACjB,kBAAkB;MACpB;;EACA;QACE,UAAU;QACV,mBAAmB;QACnB,4BAA4B;MAC9B;;EACA;QACE,UAAU;QACV,iBAAiB;MACnB;;EACA;QACE,WAAW;QACX,sBAAsB;;MAiBxB;;EAhBE;UACE,YAAY;UACZ,iBAAiB;UACjB,6BAA6B;QAQ/B;;EAPE;YACE,iBAAiB;YACjB,4BAA4B;UAC9B;;EACA;YACE,kBAAkB;UACpB;;EAEF;UACE,mBAAmB;QACrB;;EAON;IACE,WAAW;IACX,WAAW;IACX,wBAAwB;IACxB,cAAc;EAChB;;EAEA;IACE,WAAW;IACX,mBAAmB;EACrB;;EAEA;IACE,kBAAkB;EA8BpB;;EA5BE;MACE,WAAW;MACX,YAAY;MACZ,6BAA6B;MAC7B,8BAA8B;MAC9B,+BAA+B;MAC/B,gCAAgC;MAChC,kBAAkB;MAClB,0BAA0B;MAC1B,uBAAuB;MACvB,sBAAsB;MACtB,qBAAqB;MACrB,kDAAoC;MACpC,0DAA4C;IAC9C;;EAEA;MACE,WAAW;MACX,YAAY;MACZ,yBAAyB;MACzB,kBAAkB;IACpB;;EAEA;MACE,kBAAkB;MAClB,QAAQ;MACR,SAAS;IACX;;EAGF;IACE,aAAa;IACb,2BAA2B;IAC3B,mBAAmB;EAUrB;;EAPI;QACE,0BAA0B;MAC5B;;ECzHN;;MD2HM,iBAAiB;MACjB;CC5HN;;EDiII;MACE,aAAa;MACb,WAAW;MACX,8BAA8B;MAC9B,mBAAmB;IAKrB;;EAHE;QACE,kBAAkB;MACpB;;EAGF;MACE,WAAW;MACX,iBAAiB;MACjB,aAAa;MACb,8BAA8B;MAC9B,gBAAgB;IAsBlB;;EApBE;QACE,UAAU;QACV,aAAa;QACb,mBAAmB;QACnB,kBAAkB;QAClB,iBAAiB;QACjB,kBAAkB;MASpB;;EAPE;UACE,WAAW;UACX,aAAa;UACb,aAAa;UACb,uBAAuB;UACvB,mBAAmB;QACrB;;EAGF;QACE,OAAO;MACT;;AAKN;EACE,8BAA8B;EAC9B,eAAe;EACf,cAAc;EACd,iBAAiB;EACjB,kBAAkB;AACpB;;AAEA;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,eAAe;EACf,iBAAiB;AACnB;;AAEA;EACE,eAAe;EACf,gBAAgB;EAChB,mBAAmB;AACrB;;AAEA;EACE,mBAAmB;EACnB,YAAY;EACZ,yBAAyB;AAC3B;;AAEA;EACE,iBAAiB;AACnB;;AAEA;EACE,mBAAmB;EACnB,YAAY;EACZ,yBAAyB;AAC3B;;AAEA;EACE,wBAAwB;AAC1B;;AAEA;EACE,WAAW;EACX,eAAe;AA4BjB;;AA1BE;IACE,WAAW;IACX,eAAe;IACf,iBAAiB;IACjB,kBAAkB;IAClB,8BAA8B;EAChC;;AAEA;IACE,WAAW;IACX,aAAa;IACb,2BAA2B;IAC3B,eAAe;IACf,WAAW;IACX,iBAAiB;IACjB,kBAAkB;EAUpB;;AARE;MACE,SAAS;MACT,kBAAkB;IACpB;;AAEA;MACE,UAAU;IACZ;;AAIJ;EACE,YAAY;AAUd;;AATE;IACE,iBAAiB;IACjB,kBAAkB;EACpB;;AACA;IACE,WAAW;IACX,YAAY;IACZ,sBAAsB;EACxB;;AAGF;EACE,WAAW;EACX,gBAAgB;EAChB,mBAAmB;EACnB,aAAa;AACf;;AAEA;EACE,2BAA2B;AAC7B;;AAEA;EACE;IACE,uBAAuB;EACzB;;EAEA;IACE,yBAAyB;EAC3B;AACF;;AARA;EACE;IACE,uBAAuB;EACzB;;EAEA;IACE,yBAAyB;EAC3B;AACF",sourcesContent:[`.warper {
  padding: 0 0 40px 0;

  .charts {
    margin-top: 16px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    > div {
      padding: 6px;
    }
  }

  .stableCharts {
    padding-bottom: 16px;
    /* box-shadow: 0 1px 4px 0 rgba(0, 0, 0, 0.13); */
    margin-bottom: 12px;
    border-bottom: 1px solid #ddd;

    .stableItem {
      /* height: 320px; */
      display: flex;
      justify-content: space-between;
      align-items: center;

      .stableIcon {
        float: right;
        margin-right: 16px;
      }
      .scTitle {
        font-size: 14px;
        color: #333;
        font-weight: bold;
        margin-bottom: 8px;
      }
      .sCharts {
        width: 68%;
        padding-right: 10px;
        border-right: 1px solid #ddd;
      }
      .sTableInfo {
        width: 30%;
        margin-left: 20px;
      }
      .sTable {
        width: 100%;
        border: 1px solid #ddd;
        tr {
          height: 32px;
          line-height: 32px;
          border-bottom: 1px solid #ddd;
          th, td {
            padding: 2px 12px;
            border-right: 1px solid #ddd;
          }
          td {
            text-align: center;
          }
        }
        th {
          background: #FAFAFA;
        }
        
      }
    }
    
  }

  .line {
    width: 100%;
    height: 1px;
    background: #dedede 100%;
    margin: 25px 0;
  }

  .strategies {
    width: 100%;
    margin-bottom: 32px;
  }

  .iconContent {
    position: relative;

    .circle {
      width: 28px;
      height: 28px;
      border-top: 2px solid #dde4ed;
      border-left: 2px solid #dde4ed;
      border-right: 2px solid #1e8e3e;
      border-bottom: 2px solid #1e8e3e;
      border-radius: 50%;
      -webkit-border-radius: 50%;
      -moz-border-radius: 50%;
      -ms-border-radius: 50%;
      -o-border-radius: 50%;
      animation: circle 2s linear infinite;
      -webkit-animation: circle 2s linear infinite;
    }

    .circleStill {
      width: 28px;
      height: 28px;
      border: 2px solid #1e8e3e;
      border-radius: 50%;
    }

    .svg {
      position: absolute;
      top: 6px;
      left: 7px;
    }
  }

  .triggered {
    display: flex;
    justify-content: flex-start;
    align-items: center;

    .svgFail {
      &::before {
        font-size: 14px !important;
      }

      margin-right: 8px;
      color: #f15533;
    }
  }

  .taskDetail {
    .timeRange {
      display: flex;
      width: 100%;
      justify-content: space-between;
      align-items: center;

      .start {
        margin-right: 30px;
      }
    }

    .taskContent {
      width: 100%;
      min-height: 300px;
      display: flex;
      justify-content: space-between;
      margin-top: 10px;

      .flows {
        width: 70%;
        padding: 24px;
        background: #f7f7f7;
        margin-right: 24px;
        max-height: 300px;
        overflow-y: scroll;

        .flowLoading {
          width: 100%;
          height: 300px;
          display: flex;
          justify-content: center;
          align-items: center;
        }
      }

      .taskInfo {
        flex: 1;
      }
    }
  }
}

.titleTips {
  font-family: PingFangSC-Medium;
  font-size: 14px;
  color: #333333;
  line-height: 32px;
  margin-bottom: 8px;
}

.ruleDetail {
  font-family: PingFangSC-Regular;
  font-size: 12px;
  color: #0070cc;
  cursor: pointer;
  line-height: 50px;
}

.showStrategy {
  font-size: 14px;
  margin-top: 15px;
  margin-bottom: 15px;
}

.showTolerance {
  background: #f7f7f7;
  padding: 8px;
  border: 1px solid #f5f5f5;
}

.tolerance {
  line-height: 20px;
}

.showRules {
  background: #f7f7f7;
  padding: 8px;
  border: 1px solid #f5f5f5;
}

.buttonSecond {
  margin: 0 8px !important;
}

.feedBack {
  width: 100%;
  padding: 20px 0;

  .feedTitle {
    color: #333;
    font-size: 14px;
    line-height: 32px;
    margin-bottom: 8px;
    font-family: PingFangSC-Medium;
  }

  .feedItem {
    width: 100%;
    display: flex;
    justify-content: flex-start;
    font-size: 12px;
    color: #555;
    line-height: 20px;
    margin-bottom: 8px;

    .label {
      width: 7%;
      margin-right: 24px;
    }

    .value {
      width: 90%;
    }
  }
}

.dependenceDialog {
  width: 640px;
  :global(.next-dialog-body) {
    max-height: 400px;
    overflow-y: scroll;
  }
  .DialogFrom {
    width: 100%;
    height: 100%;
    /* overflow: scroll; */
  }
}

.specialDom {
  width: 100%;
  min-height: 57px;
  background: #f7f7f7;
  padding: 12px;
}

.normalBtn {
  margin-left: 8px !important;
}

@keyframes circle {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}`,null],sourceRoot:""}]),m.locals={warper:"index__warper__18o4L",charts:"index__charts__ta2QJ",stableCharts:"index__stableCharts__vS9OL",stableItem:"index__stableItem__Hueln",stableIcon:"index__stableIcon__e2BNZ",scTitle:"index__scTitle__hUGUT",sCharts:"index__sCharts__1Yjxf",sTableInfo:"index__sTableInfo__RkLuU",sTable:"index__sTable__7wGPw",line:"index__line__KEsCJ",strategies:"index__strategies__9VerZ",iconContent:"index__iconContent__06krr",circle:"index__circle__IaeAk",circleStill:"index__circleStill__YOdeU",svg:"index__svg__1mAMv",triggered:"index__triggered__gBwsf",svgFail:"index__svgFail__-mG1q",taskDetail:"index__taskDetail__j-M5+",timeRange:"index__timeRange__i8+aE",start:"index__start__k3EjF",taskContent:"index__taskContent__Fhk-G",flows:"index__flows__ZUJPC",flowLoading:"index__flowLoading__MZAg9",taskInfo:"index__taskInfo__d8za1",titleTips:"index__titleTips__ks8bG",ruleDetail:"index__ruleDetail__Q4WNq",showStrategy:"index__showStrategy__1be-q",showTolerance:"index__showTolerance__VutSA",tolerance:"index__tolerance__vZPQS",showRules:"index__showRules__wL5Tq",buttonSecond:"index__buttonSecond__byH23",feedBack:"index__feedBack__YT3vN",feedTitle:"index__feedTitle__NxZal",feedItem:"index__feedItem__cKpEd",label:"index__label__tss0J",value:"index__value__IphH3",dependenceDialog:"index__dependenceDialog__VJA7C",DialogFrom:"index__DialogFrom__SVrhU",specialDom:"index__specialDom__xMBKC",normalBtn:"index__normalBtn__7DIS1"};const f=m},56061:(w,C,e)=>{"use strict";e.d(C,{Z:()=>f});var d=e(60994),I=e.n(d),c=e(93476),y=e.n(c),m=y()(I());m.push([w.id,`.index__root__QuvEG {
  position: relative;
  margin-top: 10px;
  height: 250px;
}

.index__tip__VAOdv {
  font-family: PingFangSC-Regular;
  background: #FAFBFC;
  line-height: 25px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
  color: #9B9B9B;
}

.index__metric__GF2bR {
  padding: 16px;
}

.index__empty__kFm3Z {
  position: absolute;
  top: 76px;
  left: 0;
  background: rgba(255, 255, 255, 0.8);
  width: 100%;
  height: 126px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

.index__empty__kFm3Z.index__loading__VWgiy {
    z-index: 2;
  }

.index__buttonGroup__w7Gha {
  font-family: PingFangSC-Regular;
  line-height: 30px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.index__buttonGroup__w7Gha.index__fullscreenMode__a9zwi {
    justify-content: flex-end;
  }

.index__fullscreenBtn__VVRI1 {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-left: 15px;
  cursor: pointer;
  color: #1296DB;
}

.index__fullscreenBtn__VVRI1 span {
    margin-right: 3px;
  }

.index__refreshBtn__3MzNS {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  cursor: pointer;
  color: #1296DB;
}

.index__refreshBtn__3MzNS span {
    margin-right: 3px;
  }

.index__DialogContent__n9KD3 {
  height: 450px !important;
}

.index__DialogContent__n9KD3 .next-dialog-body {
    overflow-y: initial !important;
  }`,"",{version:3,sources:["webpack://./pages/Chaos/Experiment/ExperimentTask/plugins/ChartContainer/index.css"],names:[],mappings:"AAAA;EACE,kBAAkB;EAClB,gBAAgB;EAChB,aAAa;AACf;;AAEA;EACE,+BAA+B;EAC/B,mBAAmB;EACnB,iBAAiB;EACjB,aAAa;EACb,mBAAmB;EACnB,uBAAuB;EACvB,mBAAmB;EACnB,kBAAkB;EAClB,cAAc;AAChB;;AAEA;EACE,aAAa;AACf;;AAEA;EACE,kBAAkB;EAClB,SAAS;EACT,OAAO;EACP,oCAAoC;EACpC,WAAW;EACX,aAAa;EACb,aAAa;EACb,mBAAmB;EACnB,uBAAuB;EACvB,mBAAmB;AAKrB;;AAHE;IACE,UAAU;EACZ;;AAGF;EACE,+BAA+B;EAC/B,iBAAiB;EACjB,aAAa;EACb,mBAAmB;EACnB,8BAA8B;EAC9B,mBAAmB;AAKrB;;AAHE;IACE,yBAAyB;EAC3B;;AAGF;EACE,aAAa;EACb,mBAAmB;EACnB,uBAAuB;EACvB,mBAAmB;EACnB,iBAAiB;EACjB,eAAe;EACf,cAAc;AAKhB;;AAHE;IACE,iBAAiB;EACnB;;AAGF;EACE,aAAa;EACb,mBAAmB;EACnB,uBAAuB;EACvB,mBAAmB;EACnB,kBAAkB;EAClB,eAAe;EACf,cAAc;AAKhB;;AAHE;IACE,iBAAiB;EACnB;;AAGF;EACE,wBAAwB;AAK1B;;AAHE;IACE,8BAA8B;EAChC",sourcesContent:[`.root {
  position: relative;
  margin-top: 10px;
  height: 250px;
}

.tip {
  font-family: PingFangSC-Regular;
  background: #FAFBFC;
  line-height: 25px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-bottom: 5px;
  color: #9B9B9B;
}

.metric {
  padding: 16px;
}

.empty {
  position: absolute;
  top: 76px;
  left: 0;
  background: rgba(255, 255, 255, 0.8);
  width: 100%;
  height: 126px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;

  &.loading {
    z-index: 2;
  }
}

.buttonGroup {
  font-family: PingFangSC-Regular;
  line-height: 30px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  &.fullscreenMode {
    justify-content: flex-end;
  }
}

.fullscreenBtn {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-left: 15px;
  cursor: pointer;
  color: #1296DB;

  span {
    margin-right: 3px;
  }
}

.refreshBtn {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  margin-right: 15px;
  cursor: pointer;
  color: #1296DB;

  span {
    margin-right: 3px;
  }
}

.DialogContent {
  height: 450px !important;

  :global(.next-dialog-body) {
    overflow-y: initial !important;
  }
}`],sourceRoot:""}]),m.locals={root:"index__root__QuvEG",tip:"index__tip__VAOdv",metric:"index__metric__GF2bR",empty:"index__empty__kFm3Z",loading:"index__loading__VWgiy",buttonGroup:"index__buttonGroup__w7Gha",fullscreenMode:"index__fullscreenMode__a9zwi",fullscreenBtn:"index__fullscreenBtn__VVRI1",refreshBtn:"index__refreshBtn__3MzNS",DialogContent:"index__DialogContent__n9KD3"};const f=m},19002:(w,C,e)=>{"use strict";e.r(C),e.d(C,{default:()=>f});var d=e(1892),I=e.n(d),c=e(74681),y={};y.insert="head",y.singleton=!1;var m=I()(c.Z,y);const f=c.Z.locals||{}},28619:(w,C,e)=>{"use strict";e.r(C),e.d(C,{default:()=>f});var d=e(1892),I=e.n(d),c=e(10981),y={};y.insert="head",y.singleton=!1;var m=I()(c.Z,y);const f=c.Z.locals||{}},51077:(w,C,e)=>{"use strict";e.r(C),e.d(C,{default:()=>f});var d=e(1892),I=e.n(d),c=e(72430),y={};y.insert="head",y.singleton=!1;var m=I()(c.Z,y);const f=c.Z.locals||{}},34912:(w,C,e)=>{"use strict";e.r(C),e.d(C,{default:()=>f});var d=e(1892),I=e.n(d),c=e(74682),y={};y.insert="head",y.singleton=!1;var m=I()(c.Z,y);const f=c.Z.locals||{}},39151:(w,C,e)=>{"use strict";e.r(C),e.d(C,{default:()=>f});var d=e(1892),I=e.n(d),c=e(56061),y={};y.insert="head",y.singleton=!1;var m=I()(c.Z,y);const f=c.Z.locals||{}}}]);

//# sourceMappingURL=821.bundle.js.map