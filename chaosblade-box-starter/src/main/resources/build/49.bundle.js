(self.webpackChunk=self.webpackChunk||[]).push([[49],{70343:function($,N,t){var E,y,h,w=t(67394);(function(s,d){if(!0)!(y=[N,t(14798)],E=d,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(s,d){"use strict";var M=t(67971);w(s,"__esModule",{value:!0}),s.SearchOptions=s.SearchOptDict=s.ExperimentConstants=void 0,d=M(d);var a={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:d.default.t("Abnormal"),FAILED:d.default.t("Not as expected"),STOPPED:d.default.t("Interrupt"),SUCCESS:d.default.t("Success"),EXCEPTION:d.default.t("Abnormal"),TOTAL:d.default.t("Number of drills")};s.ExperimentConstants=a;var S={1:{name:d.default.t("Success"),status:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_SUCCESS]},2:{name:d.default.t("In progress"),status:a.EXPERIMENT_TASK_STATE_RUNNING,results:[]},3:{name:d.default.t("Interrupt"),value:"3",status:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_REJECTED,a.EXPERIMENT_TASK_RESULT_STOPPED]},4:{name:d.default.t("Not as expected"),value:"4",status:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_FAILED]},5:{name:d.default.t("Abnormal"),status:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_ERROR]}};s.SearchOptDict=S;var P=[{label:d.default.t("All")},{label:d.default.t("Success"),state:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_SUCCESS]},{label:d.default.t("In progress"),state:a.EXPERIMENT_TASK_STATE_RUNNING,results:[]},{label:d.default.t("Interrupt"),state:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_REJECTED,a.EXPERIMENT_TASK_RESULT_STOPPED]},{label:d.default.t("Not as expected"),state:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_FAILED]},{label:d.default.t("Abnormal"),state:a.EXPERIMENT_TASK_STATE_FINISHED,results:[a.EXPERIMENT_TASK_RESULT_ERROR]}];s.SearchOptions=P})},28697:function($,N,t){var E,y,h,w=t(24596),s=t(67394),d=t(93168),M=t(23587);(function(a,S){if(!0)!(y=[N,t(83452),t(77809),t(81853),t(98238),t(27378),t(66697),t(98784),t(60042),t(61320),t(65354),t(14870),t(49729)],E=S,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var P})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(a,S,P,m,u,l,p,g,z,e,c,r,D){"use strict";var x=t(67971);s(a,"__esModule",{value:!0}),a.default=void 0,S=x(S),P=x(P),m=x(m),u=x(u),l=i(l),p=x(p),g=x(g),z=x(z),e=x(e),c=x(c);function F(_){if(typeof d!="function")return null;var o=new d,f=new d;return(F=function(G){return G?f:o})(_)}function i(_,o){if(!o&&_&&_.__esModule)return _;if(_===null||w(_)!=="object"&&typeof _!="function")return{default:_};var f=F(o);if(f&&f.has(_))return f.get(_);var A={},G=s&&M;for(var b in _)if(b!=="default"&&Object.prototype.hasOwnProperty.call(_,b)){var Y=G?M(_,b):null;Y&&(Y.get||Y.set)?s(A,b,Y):A[b]=_[b]}return A.default=_,f&&f.set(_,A),A}function L(){var _=(0,r.useDispatch)(),o=(0,D.useQuery)("id"),f=(0,l.useState)([]),A=(0,m.default)(f,2),G=A[0],b=A[1],Y=(0,l.useRef)(!0);if((0,l.useEffect)(function(){Y.current&&(0,P.default)(regeneratorRuntime.mark(function O(){var K,C,Z;return regeneratorRuntime.wrap(function(Q){for(;;)switch(Q.prev=Q.next){case 0:return Q.next=2,_.scopesControl.getScopeInvocation({configuration_id:o});case 2:K=Q.sent,C=K.Data,Z=C===void 0?!1:C,Z&&b(B(Z));case 6:case"end":return Q.stop()}},O)}))()},[]),(0,l.useEffect)(function(){return function(){Y.current=!1}},[]),g.default.isEmpty(G))return null;function B(O){var K=[];return g.default.isEmpty(O)||g.default.sortBy(O,"time").forEach(function(C){K.push({total:C.total,time:C.time,year:(0,e.default)(C.time).get("year"),day:(0,e.default)(C.time).format("d"),month:(0,e.default)(C.time).get("month")+1,date:(0,e.default)(C.time).format("YYYY-MM-DD")})}),K}var V=g.default.groupBy(G,"month"),k=g.default.sortBy((0,S.default)(V)),X=[],J=[],v=[];return g.default.forEach(V,function(){X=g.default.concat(V[k[0]]),J=g.default.concat(V[k[1]]),v=g.default.concat(V[k[2]])}),l.default.createElement(l.default.Fragment,null,l.default.createElement("div",{className:c.default.heatMapContent},l.default.createElement("div",{className:c.default.week},l.default.createElement("div",{className:c.default.weekDay},l.default.createElement(p.default,null,"Sunday")),l.default.createElement("div",{className:c.default.weekDay},l.default.createElement(p.default,null,"Wednesday")),l.default.createElement("div",{className:c.default.weekDay},l.default.createElement(p.default,null,"Saturday"))),l.default.createElement(u.default,{data:X}),l.default.createElement(u.default,{data:J}),l.default.createElement(u.default,{data:v})),l.default.createElement("div",{className:c.default.legendContent},l.default.createElement("div",{className:c.default.itemBlock},l.default.createElement("i",{className:(0,z.default)(c.default.block,c.default.manyTotal)}),l.default.createElement("div",null,"50")),l.default.createElement("div",{className:c.default.itemBlock},l.default.createElement("i",{className:(0,z.default)(c.default.block,c.default.middleTotal)}),l.default.createElement("div",null,"21~50")),l.default.createElement("div",{className:c.default.itemBlock},l.default.createElement("i",{className:(0,z.default)(c.default.block,c.default.littleTotal)}),l.default.createElement("div",null,"1~20")),l.default.createElement("div",{className:c.default.itemBlock},l.default.createElement("i",{className:(0,z.default)(c.default.block,c.default.noTotal)}),l.default.createElement("div",null,"0"))))}var n=(0,l.memo)(L);a.default=n})},98238:function($,N,t){var E,y,h,w=t(67394);(function(s,d){if(!0)!(y=[N,t(75453),t(92243),t(27378),t(66697),t(98784),t(14798),t(61320),t(65354)],E=d,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(s,d,M,a,S,P,m,u,l){"use strict";var p=t(67971);w(s,"__esModule",{value:!0}),s.default=void 0,d=p(d),M=p(M),a=p(a),S=p(S),P=p(P),m=p(m),u=p(u),l=p(l);var g=M.default.Tooltip,z=function(r){var D=r.data;function x(){switch(D[0].month){case 1:return m.default.t("January");case 2:return m.default.t("February");case 3:return m.default.t("March");case 4:return m.default.t("April");case 5:return m.default.t("May");case 6:return m.default.t("June");case 7:return m.default.t("July");case 8:return m.default.t("August");case 9:return m.default.t("September");case 10:return m.default.t("October");case 11:return m.default.t("November");default:return m.default.t("December")}}function F(o){for(var f=[],A=0;A<o;A++)f.push({});return f}function i(o,f){var A=new Date(o,f,0);return A.getDate()}function L(){var o=D[0],f=o.year,A=o.month,G=o.day,b=P.default.last(D),Y=b.day,B=F(G),V=F(6-Y),k=[];if(A!==(0,u.default)().get("month")+1)k=P.default.concat(B,D,V);else if(!P.default.isEmpty(D)){var X=F(i(f,A)-D.length);k=P.default.concat(B,D,X)}return k}function n(o){var f=o.total;return f>50?l.default.manyTotal:f>=21&&f<=50?l.default.middleTotal:f>=1&&f<=20?l.default.littleTotal:l.default.noTotal}function _(o){var f=o.date,A=o.total;return a.default.createElement("div",{className:l.default.toolText,key:"".concat(f).concat(A)},a.default.createElement("div",{className:l.default.toolTitle},f),a.default.createElement("div",null,a.default.createElement(S.default,null,"Number of executions"),":",A))}return a.default.createElement("div",{className:l.default.monthChart},a.default.createElement("div",{className:l.default.monthTitle},x()),a.default.createElement("div",{className:l.default.monthContent},a.default.createElement("ul",{className:l.default.ulList},(0,d.default)(L()).map(function(o,f){return P.default.isEmpty(o)?a.default.createElement("div",{className:l.default.emptyContent,key:"".concat(o).concat(f)}):a.default.createElement(g,{trigger:a.default.createElement("li",{key:f,className:n(o)}),align:"r"},_(o))}))))},e=z;s.default=e})},99006:function($,N,t){var E,y,h,w=t(24596),s=t(67394),d=t(93168),M=t(23587);(function(a,S){if(!0)!(y=[N,t(77809),t(81853),t(93989),t(27378),t(98784),t(73014),t(13026),t(14870),t(49729)],E=S,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var P})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(a,S,P,m,u,l,p,g,z,e){"use strict";var c=t(67971);s(a,"__esModule",{value:!0}),a.default=void 0,S=c(S),P=c(P),m=c(m),u=D(u),l=c(l);function r(i){if(typeof d!="function")return null;var L=new d,n=new d;return(r=function(o){return o?n:L})(i)}function D(i,L){if(!L&&i&&i.__esModule)return i;if(i===null||w(i)!=="object"&&typeof i!="function")return{default:i};var n=r(L);if(n&&n.has(i))return n.get(i);var _={},o=s&&M;for(var f in i)if(f!=="default"&&Object.prototype.hasOwnProperty.call(i,f)){var A=o?M(i,f):null;A&&(A.get||A.set)?s(_,f,A):_[f]=i[f]}return _.default=i,n&&n.set(i,_),_}function x(){var i=(0,e.useQuery)("id"),L=(0,z.useDispatch)(),n=(0,u.useState)([]),_=(0,P.default)(n,2),o=_[0],f=_[1];if((0,u.useEffect)(function(){return(0,S.default)(regeneratorRuntime.mark(function X(){var J,v,O;return regeneratorRuntime.wrap(function(C){for(;;)switch(C.prev=C.next){case 0:return C.next=2,L.scopesControl.getScopeSceneFunctionCount({configuration_id:i});case 2:J=C.sent,v=J.Data,O=v===void 0?!1:v,O&&f(O);case 6:case"end":return C.stop()}},X)}))(),function(){f([])}},[]),l.default.isEmpty(o))return null;function A(X){var J=X*100;return X=J.toFixed(2)+"%",X}var G=m.default.DataView,b=p.Guide.Html,Y=new G;Y.source(o).transform({type:"percent",field:"count",dimension:"name",as:"percent"});var B={percent:{formatter:function(J){return A(J)}}},V=(0,g.getLanguage)(),k="";return V==="zh"?k="<div style=&quot;color:#333;font-size:12px;text-align: center;&quot;>\u8FD1\u4E09\u6708<br><span style=&quot;color:#333;font-size:12px&quot;>\u6F14\u7EC3\u60C5\u51B5</span></div>":k="<div style=&quot;color:#333;font-size:12px;text-align: center;&quot;>Nearly March<br><span style=&quot;color:#333;font-size:12px&quot;>Drill situation</span></div>",u.default.createElement("div",null,u.default.createElement(p.Chart,{height:145,data:Y,scale:B,padding:[0,0,0,0],forceFit:!0},u.default.createElement(p.Coord,{type:"theta",radius:.7,innerRadius:.62}),u.default.createElement(p.Axis,{name:"percent"}),u.default.createElement(p.Legend,{position:"right",offsetY:5,offsetX:-40,useHtml:!0,scroll:!0,itemTpl:'<li class="g2-legend-list-item item-{index} {checked}" data-color="{originColor}" data-value="{originValue}" style="cursor: pointer;font-size: 12px;overflow:hidden;text-overflow: ellipsis;width: 100px;white-space: nowrap;height: 16px" title={value}><i class="g2-legend-marker" style="width:10px;height:10px;border-radius:50%;display:inline-block;margin-right:10px;background-color: {color};"></i><span class="g2-legend-text" style="width:84px;">{value}</span></li>'}),u.default.createElement(p.Tooltip,{showTitle:!1,itemTpl:'<li><span style="background-color:{color};" class="g2-tooltip-marker"></span>{name}: {value}</li>'}),u.default.createElement(p.Guide,null,u.default.createElement(b,{position:["50%","50%"],html:k,alignX:"middle",alignY:"middle"})),u.default.createElement(p.Geom,{type:"intervalStack",position:"percent",color:"name",tooltip:["name*percent",function(X,J){return{name:X,value:A(J)}}],style:{lineWidth:1,stroke:"#fff"}},u.default.createElement(p.Label,{content:"percent"}))))}var F=(0,u.memo)(x);a.default=F})},70049:function($,N,t){var E,y,h,w=t(24596),s=t(14176),d=t(67394),M=t(93168),a=t(23587);(function(S,P){if(!0)!(y=[N,t(14176),t(93484),t(42499),t(17225),t(77809),t(81853),t(28697),t(27378),t(99006),t(66697),t(98784),t(60042),t(74590),t(14798),t(68055),t(65354),t(73262),t(70343),t(96291),t(99328),t(14870),t(42058),t(49729)],E=P,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var m})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(S,P,m,u,l,p,g,z,e,c,r,D,x,F,i,L,n,_,o,f,A,G,b,Y){"use strict";var B=t(67971);d(S,"__esModule",{value:!0}),S.default=void 0,P=B(P),m=B(m),u=B(u),l=B(l),p=B(p),g=B(g),z=B(z),e=k(e),c=B(c),r=B(r),D=B(D),x=B(x),F=B(F),i=B(i),L=B(L),n=B(n);function V(v){if(typeof M!="function")return null;var O=new M,K=new M;return(V=function(Z){return Z?K:O})(v)}function k(v,O){if(!O&&v&&v.__esModule)return v;if(v===null||w(v)!=="object"&&typeof v!="function")return{default:v};var K=V(O);if(K&&K.has(v))return K.get(v);var C={},Z=d&&a;for(var q in v)if(q!=="default"&&Object.prototype.hasOwnProperty.call(v,q)){var Q=Z?a(v,q):null;Q&&(Q.get||Q.set)?d(C,q,Q):C[q]=v[q]}return C.default=v,K&&K.set(v,C),C}var X=function(){var O=(0,Y.useQuery)("id"),K=(0,Y.useQuery)("type"),C=(0,G.useDispatch)(),Z=(0,b.useHistory)(),q=(0,e.useState)([]),Q=(0,g.default)(q,2),ne=Q[0],re=Q[1],Ae=(0,e.useState)([]),le=(0,g.default)(Ae,2),ae=le[0],se=le[1],me=(0,e.useState)(1),de=(0,g.default)(me,2),ee=de[0],pe=de[1],ce=(0,e.useState)(1),ue=(0,g.default)(ce,2),te=ue[0],Ce=ue[1],Re=(0,e.useState)(0),fe=(0,g.default)(Re,2),he=fe[0],Pe=fe[1],ge=(0,e.useState)(0),ie=(0,g.default)(ge,2),ve=ie[0],Ue=ie[1],Se=(0,e.useState)(),oe=(0,g.default)(Se,2),R=oe[0],xe=oe[1],Ie=(0,G.useSelector)(function(I){return{loading:I.loading.effects["scopesControl/getExperimentTaskScopes"]}}),_e=Ie.loading;if((0,e.useEffect)(function(){C.pageHeader.setTitle(R&&R.hostname),C.pageHeader.setBreadCrumbItems(f.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"experiment_scope_control",value:i.default.t("Probe Management"),path:"/chaos/experiment/scope/control"},{key:"scopes_detail",value:i.default.t("Resource Details"),path:"/chaos/scope/detail"}])),C.pageHeader.showBackArrow(!0)},[R]),(0,e.useEffect)(function(){(0,p.default)(regeneratorRuntime.mark(function I(){var W,H,T;return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,C.scopesControl.getScopeInfo({configuration_id:O,scope_type:0});case 2:W=U.sent,H=W.Data,T=H===void 0?!1:H,T&&xe(T);case 6:case"end":return U.stop()}},I)}))()},[]),(0,e.useEffect)(function(){(0,p.default)(regeneratorRuntime.mark(function I(){var W,H,T;return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,C.scopesControl.getExperimentTaskScopes({configuration_id:O,page:ee,size:10});case 2:W=U.sent,H=W.Data,T=H===void 0?!1:H,T&&(re(D.default.get(T,"data",[])),Pe(D.default.get(T,"total",0)));case 6:case"end":return U.stop()}},I)}))()},[ee]),(0,e.useEffect)(function(){K&&(0,p.default)(regeneratorRuntime.mark(function I(){var W,H,T;return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,C.scopesControl.getSearchExperimentPodsByNode({node_configuration_id:O,page:te,size:10,key:"",kub_namespace:[]});case 2:W=U.sent,H=W.Data,T=H===void 0?!1:H,T&&(se(D.default.get(T,"data",[])),Ue(D.default.get(T,"total",0)));case 6:case"end":return U.stop()}},I)}))()},[te]),!ne)return null;if(!ae)return null;var Te=function(W,H,T){var j=T.state,U=T.result;if(j===o.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED){if(U===o.ExperimentConstants.EXPERIMENT_TASK_RESULT_SUCCESS)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"select",className:(0,x.default)(n.default.onLineState,n.default.icon)}),e.default.createElement(r.default,null,"Success"));if(U===o.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"exclamationcircle-f",className:(0,x.default)(n.default.icon,n.default.offLineState)}),e.default.createElement(r.default,null,"Not as expected"));if(U===o.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"minus-circle-fill",className:(0,x.default)(n.default.icon,n.default.notInstall)}),e.default.createElement(r.default,null,"Abnormal"));if(U===o.ExperimentConstants.EXPERIMENT_TASK_RESULT_STOPPED)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"minus-circle-fill",className:(0,x.default)(n.default.icon,n.default.interrupt)}),e.default.createElement(r.default,null,"Interrupt"))}return e.default.createElement("span",null,e.default.createElement(l.default,{type:"loading",className:(0,x.default)(n.default.icon,n.default.loading)}),e.default.createElement(r.default,null,"In execution"))},Ne=function(W,H,T){var j=T.experiment_id;return e.default.createElement("a",{className:n.default.linkHref,onClick:function(){(0,A.pushUrl)(Z,"/chaos/experiment/detail",{id:j})}},W)},Be=function(W,H,T){var j=T.task_id;return e.default.createElement("a",{className:n.default.linkHref,onClick:function(){(0,A.pushUrl)(Z,"/chaos/experiment/task",{id:j})}},e.default.createElement(r.default,null,"See details"))};function Ee(I,W){W?Ce(I):pe(I)}function ye(I){(0,A.pushUrl)(Z,"/chaos/application/detail",{appId:I})}function Me(){return R&&R.app_info?e.default.createElement("div",{className:n.default.href,onClick:function(){return ye(R&&R.app_info.app_id)}},R&&R.app_info.app_name):e.default.createElement(r.default,null,"None")}function De(){return e.default.createElement("div",null,e.default.createElement("div",{className:n.default.title},e.default.createElement(r.default,null,"Host information")),e.default.createElement("div",{className:K?n.default.around:n.default.info},e.default.createElement("div",{className:n.default.left},e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"name"),":"),e.default.createElement("div",{className:n.default.value,title:R&&R.hostname},R&&R.hostname)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Number of drills"),":"),e.default.createElement("div",{className:n.default.value},R&&R.running_info&&R.running_info.total)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Public IP"),":"),e.default.createElement("div",{className:n.default.value},R&&R.public_ip)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Probe version"),":"),e.default.createElement("div",{className:n.default.value},R&&R.agent_version)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Start Time"),":"),e.default.createElement("div",{className:n.default.value},(0,F.default)(D.default.get(R,"collect_time",0))))),e.default.createElement("div",{className:n.default.right},K&&e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label}," ",e.default.createElement(r.default,null,"Cluster name"),":"),e.default.createElement("div",{className:n.default.value},R&&R.cluster_info&&R.cluster_info.cluster_name)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label}," ",e.default.createElement(r.default,null,"Operating system"),":"),e.default.createElement("div",{className:n.default.value},R&&R.os_version)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Private IP"),":"),e.default.createElement("div",{className:n.default.value},R&&R.private_ip)),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Probe Status"),":"),e.default.createElement("div",{className:n.default.value},We(R&&R.agent_status))),e.default.createElement("div",{className:n.default.item},e.default.createElement("div",{className:n.default.label},e.default.createElement(r.default,null,"Owning application"),":"),e.default.createElement("div",{className:n.default.value},Me())))))}function We(I){if(I===_.AGENT_STATUS.ONLINE)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"select",className:(0,x.default)(n.default.onLineState,n.default.icon)}),e.default.createElement(r.default,null,"Online"));if(I===_.AGENT_STATUS.WAIT_INSTALL)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"minus-circle-fill",className:(0,x.default)(n.default.icon,n.default.notInstall)}),e.default.createElement(r.default,null,"Not Installed"));if(I===_.AGENT_STATUS.OFFLINE)return e.default.createElement("span",null,e.default.createElement(l.default,{type:"exclamationcircle-f",className:(0,x.default)(n.default.icon,n.default.offLineState)}),e.default.createElement(r.default,null,"Offline"))}function Ke(){return e.default.createElement(e.default.Fragment,null,e.default.createElement("div",{className:n.default.title},e.default.createElement(r.default,null,"Drill data")),e.default.createElement("div",{className:n.default.info},e.default.createElement("div",{className:n.default.heatmap},e.default.createElement(z.default,null)),e.default.createElement("div",{className:n.default.ringChart},e.default.createElement(c.default,null))))}var we=function(W,H,T){if(!D.default.isEmpty(T)){var j=T.app_id;return e.default.createElement("span",{className:n.default.href,onClick:function(){(0,A.pushUrl)(Z,"/chaos/application/detail",{appId:j})}},W)}};function Le(){return e.default.createElement("div",{className:n.default.taskHistory},e.default.createElement("div",{className:n.default.title},e.default.createElement(r.default,null,"Pod information")),e.default.createElement(u.default,{dataSource:ae,hasBorder:!1,loading:_e,locale:(0,L.default)().Table},e.default.createElement(u.default.Column,{title:"PodIP",dataIndex:"pod_ip",width:"15%"}),e.default.createElement(u.default.Column,{title:i.default.t("PodIP").toString(),dataIndex:"pod_name"}),e.default.createElement(u.default.Column,{title:i.default.t("Cluster namespace").toString(),dataIndex:"kub_namespace"}),e.default.createElement(u.default.Column,{title:i.default.t("The application where the Pod is located").toString(),dataIndex:"app_name",cell:we}),e.default.createElement(u.default.Column,{title:i.default.t("Last heartbeat time").toString(),dataIndex:"last_heart_time",cell:F.default})),e.default.createElement(m.default,{className:n.default.pagination,current:te,total:ve,locale:(0,L.default)().Pagination,onChange:function(W){return Ee(W,"pod")}}))}return e.default.createElement("div",{className:n.default.warp},e.default.createElement("div",{className:n.default.topContent},e.default.createElement("div",{className:n.default.ipInfo},De()),e.default.createElement("div",{className:n.default.taskData},Ke())),(0,P.default)(K)===_.SCOPE_TYPE.K8S&&Le(),e.default.createElement("div",{className:n.default.taskHistory},e.default.createElement("div",{className:n.default.title},e.default.createElement(r.default,null,"Exercise record")),e.default.createElement(u.default,{dataSource:ne,hasBorder:!1,loading:_e,locale:(0,L.default)().Table},e.default.createElement(u.default.Column,{title:i.default.t("Drill name").toString(),dataIndex:"name",width:"37%",cell:Ne}),e.default.createElement(u.default.Column,{title:i.default.t("Start time").toString(),dataIndex:"startTime",cell:F.default}),e.default.createElement(u.default.Column,{title:i.default.t("End Time").toString(),dataIndex:"endTime",cell:F.default}),e.default.createElement(u.default.Column,{title:i.default.t("State").toString(),dataIndex:"state",cell:Te}),e.default.createElement(u.default.Column,{title:i.default.t("Operation").toString(),cell:Be})),e.default.createElement(m.default,{className:n.default.pagination,current:ee,total:he,locale:(0,L.default)().Pagination,onChange:function(W){return Ee(W,"")}})))},J=(0,e.memo)(X);S.default=J})},74590:function($,N,t){var E,y,h,w=t(67394);(function(s,d){if(!0)!(y=[N,t(61320)],E=d,h=typeof E=="function"?E.apply(N,y):E,h!==void 0&&($.exports=h));else var M})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(s,d){"use strict";var M=t(67971);w(s,"__esModule",{value:!0}),s.default=void 0,d=M(d);var a=function(m){return m?(0,d.default)(m).format("YYYY-MM-DD HH:mm:ss"):""},S=a;s.default=S})},28311:($,N,t)=>{"use strict";t.d(N,{Z:()=>d});var E=t(60994),y=t.n(E),h=t(93476),w=t.n(h),s=w()(y());s.push([$.id,`.index__warp__AWUKU .index__topContent__Cn0oW {
    width: 100%;
    height: 210px;
    display: flex;
    justify-content: space-between;
  }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__ipInfo__jp\\+Q1 {
      width: 42%;
      height: 100%;
      border: 1px solid #dedede;
      padding: 16px 24px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__info__ooptY {
      display: flex;
      justify-content: space-between;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__around__nahua {
      display: flex;
      justify-content: space-around;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__left__2\\+ma0 {
      /* width: 50%; */
      display: flex;
      flex-direction: column;
      align-items: flex-start;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__right__2N0Mw, .index__warp__AWUKU .index__topContent__Cn0oW .index__other__FVGP7 {
      /* width: 193px; */
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__item__IX2n9 {
      display: flex;
      justify-self: start;
      height: 30px;
      line-height: 30px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__label__mxDMz {
      font-size: 12px;
      color: #888;
      width: 72px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__value__0nStS {
      overflow: hidden;
      text-overflow: ellipsis;
      width: 156px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__heatmap__Y8TIN {
      width: 40%;
      height: 164px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__ringChart__ficZl {
      width: 60%;
      height: 164px;
    }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__ringChart__ficZl .g2-legend {
        height: 200px !important;
        overflow: auto;
        max-width: 95px !important;
      }

    .index__warp__AWUKU .index__topContent__Cn0oW .index__taskData__f4ixz {
      width: 56.4%;
      height: 210px;
      border: 1px solid #dedede;
      padding: 16px 24px;
      overflow: hidden;
    }
  .index__warp__AWUKU .index__taskHistory__aDBff {
    width: 100%;
    margin-top: 16px;
    border: 1px solid #dedede;
    padding: 16px 24px;
  }
  .index__warp__AWUKU .index__href__x31mr {
    color: #0070cc;
    cursor: pointer;
    width: 145px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .index__warp__AWUKU .index__href__x31mr:hover {
      text-decoration: underline;
    }
  .index__warp__AWUKU .index__pagination__lqZCm {
    margin-top: 8px;
    text-align: right;
  }
  .index__warp__AWUKU .index__title__ORhvt {
    font-family: PingFangSC-Medium;
    font-size: 14px;
    color: #333333;
    margin-bottom: 16px;
  }
  .index__warp__AWUKU .index__icon__EK4Gt {
    font-size: 14px;
    margin-right: 8px;
  }
  .index__warp__AWUKU .index__icon__EK4Gt::before {
      font-size: 14px !important;
      width: 14px !important;
    }
  .index__warp__AWUKU .index__onLineState__GLJtO {
    color: #1e8e3e;
  }
  .index__warp__AWUKU .index__notInstall__STx3i {
    color: #888;
  }
  .index__warp__AWUKU .index__offLineState__2lHe8 {
    color: #d93026;
  }
  .index__warp__AWUKU .index__interrupt__ImmML {
    color: #ffc440;
  }
  .index__warp__AWUKU .index__loading__SCvSk {
    color: #888;
  }
  .index__warp__AWUKU .index__linkHref__xFSuN {
    cursor: pointer;
  }
  .index__warp__AWUKU .index__linkHref__xFSuN:hover {
      text-decoration: none !important;
    }

/* // \u70ED\u529B\u56FE\u6837\u5F0F */

.index__monthChart__YHzbm {
  margin-right: 8px;
}

.index__monthChart__YHzbm .index__monthTitle__qZ1x0 {
    width: 70px;
    text-align: right;
    font-size: 12px;
    color: #333333;
    margin-bottom: 8px;
  }

.index__monthChart__YHzbm .index__monthContent__KiDRf {
    max-width: 70px;
    height: 94px;
  }

.index__monthChart__YHzbm .index__monthContent__KiDRf .index__ulList__abwmo {
      max-width: 100%;
      height: 100%;
      display: flex;
      flex-flow: column;
      flex-wrap: wrap;
      justify-content: flex-start;
    }

.index__monthChart__YHzbm .index__monthContent__KiDRf .index__ulList__abwmo li {
        width: 12px;
        height: 12px;
        background: #f5f5f5;
        border: 1px solid #fff;
        cursor: pointer;
      }

.index__monthChart__YHzbm .index__emptyContent__zpmRP {
    width: 12px;
    height: 12px;
    border: 0.5px solid #fff;
    background-color: #fff;
  }

.index__toolText__KQJ0S {
  font-size: 8px;
  color: #555;
}

.index__toolText__KQJ0S .index__toolTitle__MfQCK {
    margin-bottom: 12px;
  }

.index__heatMapContent__5RwMA {
  height: 109px;
  display: flex;
  justify-content: flex-start;
}

.index__week__LsV\\+R {
  margin-right: 12px;
  margin-top: 23px;
  font-size: 12px;
  color: #888;
}

.index__week__LsV\\+R .index__weekDay__2S486 {
    width: 24px;
    height: 36px;
  }

.index__legendContent__wJNMw {
  margin-top: 14px;
  display: flex;
  font-size: 12px;
  color: #888;
  justify-content: space-around;
  padding: 0 32px;
}

.index__legendContent__wJNMw .index__itemBlock__AJAao {
    display: flex;
    align-items: center;
  }

.index__block__ffNS1 {
  display: inline-block;
  width: 10px;
  height: 10px;
  margin-right: 4px;
}

.index__noTotal__ENIqI {
  background: #f5f5f5 !important;
}

.index__manyTotal__A1obV {
  background: #597ef7 !important;
}

.index__middleTotal__6vdGU {
  background: #85a5ff !important;
}

.index__littleTotal__7KVuS {
  background: #adc6ff !important;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/ScopesControlDetail/index.css"],names:[],mappings:"AACE;IACE,WAAW;IACX,aAAa;IACb,aAAa;IACb,8BAA8B;EA0EhC;;IAxEE;MACE,UAAU;MACV,YAAY;MACZ,yBAAyB;MACzB,kBAAkB;IACpB;;IAEA;MACE,aAAa;MACb,8BAA8B;IAChC;;IAEA;MACE,aAAa;MACb,6BAA6B;IAC/B;;IAEA;MACE,gBAAgB;MAChB,aAAa;MACb,sBAAsB;MACtB,uBAAuB;IACzB;;IAEA;MACE,kBAAkB;MAClB,aAAa;MACb,sBAAsB;MACtB,yBAAyB;IAC3B;;IAEA;MACE,aAAa;MACb,mBAAmB;MACnB,YAAY;MACZ,iBAAiB;IACnB;;IAEA;MACE,eAAe;MACf,WAAW;MACX,WAAW;IACb;;IAEA;MACE,gBAAgB;MAChB,uBAAuB;MACvB,YAAY;IACd;;IAEA;MACE,UAAU;MACV,aAAa;IACf;;IAEA;MACE,UAAU;MACV,aAAa;IAMf;;IALE;QACE,wBAAwB;QACxB,cAAc;QACd,0BAA0B;MAC5B;;IAGF;MACE,YAAY;MACZ,aAAa;MACb,yBAAyB;MACzB,kBAAkB;MAClB,gBAAgB;IAClB;EAGF;IACE,WAAW;IACX,gBAAgB;IAChB,yBAAyB;IACzB,kBAAkB;EACpB;EAEA;IACE,cAAc;IACd,eAAe;IACf,YAAY;IACZ,gBAAgB;IAChB,uBAAuB;IACvB,mBAAmB;EAKrB;EAHE;MACE,0BAA0B;IAC5B;EAGF;IACE,eAAe;IACf,iBAAiB;EACnB;EAEA;IACE,8BAA8B;IAC9B,eAAe;IACf,cAAc;IACd,mBAAmB;EACrB;EAEA;IACE,eAAe;IACf,iBAAiB;EAMnB;EAJE;MACE,0BAA0B;MAC1B,sBAAsB;IACxB;EAGF;IACE,cAAc;EAChB;EAEA;IACE,WAAW;EACb;EAEA;IACE,cAAc;EAChB;EAEA;IACE,cAAc;EAChB;EAEA;IACE,WAAW;EACb;EAEA;IACE,eAAe;EAIjB;EAHE;MACE,gCAAgC;IAClC;;AAIJ,aAAa;;AAEb;EACE,iBAAiB;AAsCnB;;AApCE;IACE,WAAW;IACX,iBAAiB;IACjB,eAAe;IACf,cAAc;IACd,kBAAkB;EACpB;;AAEA;IACE,eAAe;IACf,YAAY;EAkBd;;AAhBE;MACE,eAAe;MACf,YAAY;MACZ,aAAa;MACb,iBAAiB;MACjB,eAAe;MACf,2BAA2B;IAS7B;;AAPE;QACE,WAAW;QACX,YAAY;QACZ,mBAAmB;QACnB,sBAAsB;QACtB,eAAe;MACjB;;AAIJ;IACE,WAAW;IACX,YAAY;IACZ,wBAAwB;IACxB,sBAAsB;EACxB;;AAGF;EACE,cAAc;EACd,WAAW;AAKb;;AAHE;IACE,mBAAmB;EACrB;;AAGF;EACE,aAAa;EACb,aAAa;EACb,2BAA2B;AAC7B;;AAEA;EACE,kBAAkB;EAClB,gBAAgB;EAChB,eAAe;EACf,WAAW;AAMb;;AAJE;IACE,WAAW;IACX,YAAY;EACd;;AAGF;EACE,gBAAgB;EAChB,aAAa;EACb,eAAe;EACf,WAAW;EACX,6BAA6B;EAC7B,eAAe;AAMjB;;AAJE;IACE,aAAa;IACb,mBAAmB;EACrB;;AAGF;EACE,qBAAqB;EACrB,WAAW;EACX,YAAY;EACZ,iBAAiB;AACnB;;AAEA;EACE,8BAA8B;AAChC;;AAEA;EACE,8BAA8B;AAChC;;AAEA;EACE,8BAA8B;AAChC;;AAEA;EACE,8BAA8B;AAChC",sourcesContent:[`.warp {
  .topContent {
    width: 100%;
    height: 210px;
    display: flex;
    justify-content: space-between;

    .ipInfo {
      width: 42%;
      height: 100%;
      border: 1px solid #dedede;
      padding: 16px 24px;
    }

    .info {
      display: flex;
      justify-content: space-between;
    }

    .around {
      display: flex;
      justify-content: space-around;
    }

    .left {
      /* width: 50%; */
      display: flex;
      flex-direction: column;
      align-items: flex-start;
    }

    .right, .other {
      /* width: 193px; */
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
    }

    .item {
      display: flex;
      justify-self: start;
      height: 30px;
      line-height: 30px;
    }

    .label {
      font-size: 12px;
      color: #888;
      width: 72px;
    }

    .value {
      overflow: hidden;
      text-overflow: ellipsis;
      width: 156px;
    }

    .heatmap {
      width: 40%;
      height: 164px;
    }

    .ringChart {
      width: 60%;
      height: 164px;
      :global(.g2-legend) {
        height: 200px !important;
        overflow: auto;
        max-width: 95px !important;
      }
    }

    .taskData {
      width: 56.4%;
      height: 210px;
      border: 1px solid #dedede;
      padding: 16px 24px;
      overflow: hidden;
    }
  }

  .taskHistory {
    width: 100%;
    margin-top: 16px;
    border: 1px solid #dedede;
    padding: 16px 24px;
  }

  .href {
    color: #0070cc;
    cursor: pointer;
    width: 145px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;

    &:hover {
      text-decoration: underline;
    }
  }

  .pagination {
    margin-top: 8px;
    text-align: right;
  }

  .title {
    font-family: PingFangSC-Medium;
    font-size: 14px;
    color: #333333;
    margin-bottom: 16px;
  }

  .icon {
    font-size: 14px;
    margin-right: 8px;

    &::before {
      font-size: 14px !important;
      width: 14px !important;
    }
  }

  .onLineState {
    color: #1e8e3e;
  }

  .notInstall {
    color: #888;
  }

  .offLineState {
    color: #d93026;
  }

  .interrupt {
    color: #ffc440;
  }

  .loading {
    color: #888;
  }

  .linkHref {
    cursor: pointer;
    &:hover {
      text-decoration: none !important;
    }
  }
}

/* // \u70ED\u529B\u56FE\u6837\u5F0F */

.monthChart {
  margin-right: 8px;

  .monthTitle {
    width: 70px;
    text-align: right;
    font-size: 12px;
    color: #333333;
    margin-bottom: 8px;
  }

  .monthContent {
    max-width: 70px;
    height: 94px;

    .ulList {
      max-width: 100%;
      height: 100%;
      display: flex;
      flex-flow: column;
      flex-wrap: wrap;
      justify-content: flex-start;

      li {
        width: 12px;
        height: 12px;
        background: #f5f5f5;
        border: 1px solid #fff;
        cursor: pointer;
      }
    }
  }

  .emptyContent {
    width: 12px;
    height: 12px;
    border: 0.5px solid #fff;
    background-color: #fff;
  }
}

.toolText {
  font-size: 8px;
  color: #555;

  .toolTitle {
    margin-bottom: 12px;
  }
}

.heatMapContent {
  height: 109px;
  display: flex;
  justify-content: flex-start;
}

.week {
  margin-right: 12px;
  margin-top: 23px;
  font-size: 12px;
  color: #888;

  .weekDay {
    width: 24px;
    height: 36px;
  }
}

.legendContent {
  margin-top: 14px;
  display: flex;
  font-size: 12px;
  color: #888;
  justify-content: space-around;
  padding: 0 32px;

  .itemBlock {
    display: flex;
    align-items: center;
  }
}

.block {
  display: inline-block;
  width: 10px;
  height: 10px;
  margin-right: 4px;
}

.noTotal {
  background: #f5f5f5 !important;
}

.manyTotal {
  background: #597ef7 !important;
}

.middleTotal {
  background: #85a5ff !important;
}

.littleTotal {
  background: #adc6ff !important;
}
`],sourceRoot:""}]),s.locals={warp:"index__warp__AWUKU",topContent:"index__topContent__Cn0oW",ipInfo:"index__ipInfo__jp+Q1",info:"index__info__ooptY",around:"index__around__nahua",left:"index__left__2+ma0",right:"index__right__2N0Mw",other:"index__other__FVGP7",item:"index__item__IX2n9",label:"index__label__mxDMz",value:"index__value__0nStS",heatmap:"index__heatmap__Y8TIN",ringChart:"index__ringChart__ficZl",taskData:"index__taskData__f4ixz",taskHistory:"index__taskHistory__aDBff",href:"index__href__x31mr",pagination:"index__pagination__lqZCm",title:"index__title__ORhvt",icon:"index__icon__EK4Gt",onLineState:"index__onLineState__GLJtO",notInstall:"index__notInstall__STx3i",offLineState:"index__offLineState__2lHe8",interrupt:"index__interrupt__ImmML",loading:"index__loading__SCvSk",linkHref:"index__linkHref__xFSuN",monthChart:"index__monthChart__YHzbm",monthTitle:"index__monthTitle__qZ1x0",monthContent:"index__monthContent__KiDRf",ulList:"index__ulList__abwmo",emptyContent:"index__emptyContent__zpmRP",toolText:"index__toolText__KQJ0S",toolTitle:"index__toolTitle__MfQCK",heatMapContent:"index__heatMapContent__5RwMA",week:"index__week__LsV+R",weekDay:"index__weekDay__2S486",legendContent:"index__legendContent__wJNMw",itemBlock:"index__itemBlock__AJAao",block:"index__block__ffNS1",noTotal:"index__noTotal__ENIqI",manyTotal:"index__manyTotal__A1obV",middleTotal:"index__middleTotal__6vdGU",littleTotal:"index__littleTotal__7KVuS"};const d=s},65354:($,N,t)=>{"use strict";t.r(N),t.d(N,{default:()=>d});var E=t(1892),y=t.n(E),h=t(28311),w={};w.insert="head",w.singleton=!1;var s=y()(h.Z,w);const d=h.Z.locals||{}}}]);

//# sourceMappingURL=49.bundle.js.map