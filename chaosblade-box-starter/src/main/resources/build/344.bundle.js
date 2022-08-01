(self.webpackChunk=self.webpackChunk||[]).push([[344],{70343:function(k,f,e){var r,h,d,p=e(67394);(function(i,n){if(!0)!(h=[f,e(14798)],r=n,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(i,n){"use strict";var l=e(67971);p(i,"__esModule",{value:!0}),i.SearchOptions=i.SearchOptDict=i.ExperimentConstants=void 0,n=l(n);var o={EXPERIMENT_STATE_DRAFT:"DRAFT",EXPERIMENT_STATE_READY:"READY",EXPERIMENT_STATE_RUNNING:"RUNNING",EXPERIMENT_STATE_SYNC:"SYNC_WAIT_EDIT",EXPERIMENT_TASK_RESULT_SUCCESS:"SUCCESS",EXPERIMENT_TASK_RESULT_FAILED:"FAILED",EXPERIMENT_TASK_RESULT_REJECTED:"REJECTED",EXPERIMENT_TASK_RESULT_ERROR:"ERROR",EXPERIMENT_TASK_RESULT_STOPPED:"STOPPED",EXPERIMENT_TASK_STATE_FINISHED:"FINISHED",EXPERIMENT_TASK_STATE_RUNNING:"RUNNING",EXPERIMENT_TASK_STATE_STOPPING:"STOPPING",EXPERIMENT_TASK_STATE_READY:"READY",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING:"USER_CHECK_STATE_WAITING",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_PASSED:"USER_CHECK_STATE_PASSED",EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_FAILED:"USER_CHECK_STATE_FAILED",EXPERIMENT_RELATION_TYPE_EMERGENCY_SCENE:"emergency_scene",EXPERIMENT_RELATION_TYPE_OWNER:"owner",ERROR:n.default.t("Abnormal"),FAILED:n.default.t("Not as expected"),STOPPED:n.default.t("Interrupt"),SUCCESS:n.default.t("Success"),EXCEPTION:n.default.t("Abnormal"),TOTAL:n.default.t("Number of drills")};i.ExperimentConstants=o;var u={1:{name:n.default.t("Success"),status:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_SUCCESS]},2:{name:n.default.t("In progress"),status:o.EXPERIMENT_TASK_STATE_RUNNING,results:[]},3:{name:n.default.t("Interrupt"),value:"3",status:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_REJECTED,o.EXPERIMENT_TASK_RESULT_STOPPED]},4:{name:n.default.t("Not as expected"),value:"4",status:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_FAILED]},5:{name:n.default.t("Abnormal"),status:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_ERROR]}};i.SearchOptDict=u;var O=[{label:n.default.t("All")},{label:n.default.t("Success"),state:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_SUCCESS]},{label:n.default.t("In progress"),state:o.EXPERIMENT_TASK_STATE_RUNNING,results:[]},{label:n.default.t("Interrupt"),state:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_REJECTED,o.EXPERIMENT_TASK_RESULT_STOPPED]},{label:n.default.t("Not as expected"),state:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_FAILED]},{label:n.default.t("Abnormal"),state:o.EXPERIMENT_TASK_STATE_FINISHED,results:[o.EXPERIMENT_TASK_RESULT_ERROR]}];i.SearchOptions=O})},95001:function(k,f,e){var r,h,d,p=e(24596),i=e(67394),n=e(93168),l=e(23587);(function(o,u){if(!0)!(h=[f,e(17534),e(12955),e(28757),e(77809),e(81853),e(27378),e(66697),e(14798),e(68055),e(29522),e(99328),e(14870)],r=u,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var O})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,u,O,H,L,z,A,w,S,ne,D,ae,j){"use strict";var Y=e(67971);i(o,"__esModule",{value:!0}),o.default=void 0,u=Y(u),O=Y(O),H=Y(H),L=Y(L),z=Y(z),A=t(A),w=Y(w),S=Y(S),ne=Y(ne),D=Y(D);function ee(c){if(typeof n!="function")return null;var s=new n,T=new n;return(ee=function(P){return P?T:s})(c)}function t(c,s){if(!s&&c&&c.__esModule)return c;if(c===null||p(c)!=="object"&&typeof c!="function")return{default:c};var T=ee(s);if(T&&T.has(c))return T.get(c);var v={},P=i&&l;for(var K in c)if(K!=="default"&&Object.prototype.hasOwnProperty.call(c,K)){var oe=P?l(c,K):null;oe&&(oe.get||oe.set)?i(v,K,oe):v[K]=c[K]}return v.default=c,T&&T.set(c,v),v}var M=function(s){var T=(0,ae.getParams)("workspaceId"),v=(0,j.useDispatch)(),P=(0,A.useState)([]),K=(0,z.default)(P,2),oe=K[0],a=K[1],pe=(0,A.useState)([]),ge=(0,z.default)(pe,2),N=ge[0],de=ge[1],se=(0,A.useState)(""),_e=(0,z.default)(se,2),ue=_e[0],De=_e[1],me=(0,A.useState)(1),Ce=(0,z.default)(me,2),Be=Ce[0],Ee=Ce[1];(0,A.useEffect)(function(){(0,L.default)(regeneratorRuntime.mark(function le(){var y,$,x;return regeneratorRuntime.wrap(function(b){for(;;)switch(b.prev=b.next){case 0:if(!(T&&s.visible)){b.next=7;break}return b.next=3,v.experimentList.searchExperiments({searchKey:ue,page:Be,workspaceId:T});case 3:y=b.sent,$=y.Data,x=$===void 0?!1:$,x&&a(oe.concat(x));case 7:case"end":return b.stop()}},le)}))()},[ue,Be,s.visible]);function V(){return I.apply(this,arguments)}function I(){return I=(0,L.default)(regeneratorRuntime.mark(function le(){var y,$,x;return regeneratorRuntime.wrap(function(b){for(;;)switch(b.prev=b.next){case 0:return b.next=2,v.experimentList.addWorkspaceExperiment({workspaceId:T,workspaceExperimentList:N});case 2:y=b.sent,$=y.Success,x=y.Data,$&&(x.duplicateExperiments.length!==0?u.default.error(S.default.t("This walkthrough already exists, please do not add it again")):(u.default.success(S.default.t("Added successfully")),s.getExperimentTotals()));case 6:case"end":return b.stop()}},le)})),I.apply(this,arguments)}function te(le){return A.default.createElement("div",null,A.default.createElement("div",null,le.label),A.default.createElement("div",{className:D.default.workspaceName},A.default.createElement(w.default,null,"From space"),": ",le.workspaceName))}function fe(le,y,$){de($.map(function(x){return{experimentId:x.value,experimentName:x.label}}))}function Re(le){De(le)}function ye(le){var y=le.target.scrollHeight,$=le.target.clientHeight,x=le.target.scrollTop;x+$===y&&Ee(Be+1)}function Ne(){V(),s.onCancel&&s.onCancel()}return A.default.createElement(O.default,{visible:s.visible,title:S.default.t("Add walkthrough").toString(),onOk:Ne,onCancel:s.onCancel,onClose:s.onCancel,locale:(0,ne.default)().Dialog},A.default.createElement("div",{className:D.default.warp},A.default.createElement("div",{className:D.default.top},A.default.createElement(w.default,null,"Walkthroughs can be selected to add to this space from other spaces with Edit Walkthrough permissions")),A.default.createElement("div",{className:D.default.item},A.default.createElement("span",{className:D.default.left},A.default.createElement(w.default,null,"Choose a walkthrough")),A.default.createElement(H.default,{autoHighlightFirstItem:!1,placeholder:S.default.t("Please select the drill you want to add").toString(),className:D.default.select,notFoundContent:S.default.t("There are currently no exercises to choose from").toString(),mode:"multiple",showSearch:!0,dataSource:oe.map(function(le){return{label:le.experimentName,value:le.experimentId,workspaceName:le.workspaceName}}),itemRender:te,popupClassName:D.default.popup,onChange:fe,onSearch:Re,menuProps:{onScroll:ye},locale:(0,ne.default)().Select}))))},m=M;o.default=m})},31157:function(k,f,e){var r,h,d,p=e(24596),i=e(67394),n=e(93168),l=e(23587);(function(o,u){if(!0)!(h=[f,e(92243),e(17534),e(15286),e(72153),e(81853),e(27378),e(66697),e(14798),e(53256)],r=u,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var O})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,u,O,H,L,z,A,w,S,ne){"use strict";var D=e(67971);i(o,"__esModule",{value:!0}),o.default=void 0,u=D(u),O=D(O),H=D(H),L=D(L),z=D(z),A=j(A),w=D(w),S=D(S),ne=D(ne);function ae(t){if(typeof n!="function")return null;var M=new n,m=new n;return(ae=function(s){return s?m:M})(t)}function j(t,M){if(!M&&t&&t.__esModule)return t;if(t===null||p(t)!=="object"&&typeof t!="function")return{default:t};var m=ae(M);if(m&&m.has(t))return m.get(t);var c={},s=i&&l;for(var T in t)if(T!=="default"&&Object.prototype.hasOwnProperty.call(t,T)){var v=s?l(t,T):null;v&&(v.get||v.set)?i(c,T,v):c[T]=t[T]}return c.default=t,m&&m.set(t,c),c}var Y=function(M){var m=M.experiment,c=m.experimentId,s=m.name,T=(0,A.useState)(!1),v=(0,z.default)(T,2),P=v[0],K=v[1],oe=(0,A.useState)("".concat(S.default.t("[copy]")).concat(s)),a=(0,z.default)(oe,2),pe=a[0],ge=a[1];return A.default.createElement(u.default,{visible:P,trigger:A.default.createElement(L.default,{disabled:M.disable,className:ne.default.opt,onClick:function(){return K(!P)},text:!0,type:"primary"},A.default.createElement(w.default,null,"Copy")),triggerType:"click",needAdjust:!0,closable:!1},A.default.createElement("p",{className:ne.default.cloneTitle},A.default.createElement(w.default,null,"Drill name")),A.default.createElement(H.default,{hasClear:!0,maxLength:20,onChange:function(de){return ge(de)},value:pe}),A.default.createElement("div",{className:ne.default.cloneBtnRow},A.default.createElement(L.default,{type:"primary",onClick:function(){pe?(M.onSubmit({experimentId:c,name:pe}),K(!1)):O.default.error(S.default.t("Please enter a drill name").toString())}},A.default.createElement(w.default,null,"Confirm")),A.default.createElement(L.default,{type:"normal",onClick:function(){return K(!1)}},A.default.createElement(w.default,null,"cancel"))))},ee=Y;o.default=ee})},2484:function(k,f,e){var r,h,d,p=e(67394),i=e(83452),n=e(95315),l=e(23587),o=e(63774),u=e(92937);(function(O,H){if(!0)!(h=[f,e(75453),e(17534),e(93484),e(12955),e(72153),e(17225),e(36939),e(92243),e(77809),e(57379),e(42499),e(31157),e(27378),e(66697),e(98784),e(60042),e(14798),e(68055),e(9394),e(53256),e(70343),e(99328),e(17640),e(14870),e(42058)],r=H,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var L})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(O,H,L,z,A,w,S,ne,D,ae,j,Y,ee,t,M,m,c,s,T,v,P,K,oe,a,pe,ge){"use strict";var N=e(67971);p(O,"__esModule",{value:!0}),O.default=void 0,H=N(H),L=N(L),z=N(z),A=N(A),w=N(w),S=N(S),ne=N(ne),D=N(D),ae=N(ae),j=N(j),Y=N(Y),ee=N(ee),t=N(t),M=N(M),m=N(m),c=N(c),s=N(s),T=N(T),v=N(v),P=N(P);function de(Ee,V){var I=i(Ee);if(n){var te=n(Ee);V&&(te=te.filter(function(fe){return l(Ee,fe).enumerable})),I.push.apply(I,te)}return I}function se(Ee){for(var V=1;V<arguments.length;V++){var I=arguments[V]!=null?arguments[V]:{};V%2?de(Object(I),!0).forEach(function(te){(0,j.default)(Ee,te,I[te])}):o?u(Ee,o(I)):de(Object(I)).forEach(function(te){p(Ee,te,l(I,te))})}return Ee}var _e=Y.default.Column,ue=2,De=(0,v.default)({count:26,hue:"#00C1DE"}),me=function(){console.log()},Ce=function(V){var I=(0,ge.useHistory)(),te=(0,pe.useDispatch)(),fe=(0,oe.getParams)("workspaceId"),Re=(0,pe.useSelector)(function(C){return se(se({},C.experimentList.experiments),{},{loading:C.loading.effects["experimentList/getExperimentList"]})}),ye=Re.data,Ne=Re.total,le=Re.loading;function y(C){return $.apply(this,arguments)}function $(){return $=(0,ae.default)(regeneratorRuntime.mark(function C(_){var g,B;return regeneratorRuntime.wrap(function(R){for(;;)switch(R.prev=R.next){case 0:return R.next=2,te.experimentList.startExperiment({experimentId:_});case 2:g=R.sent,B=g.Data.taskId,B&&V.getExperimentTotals();case 5:case"end":return R.stop()}},C)})),$.apply(this,arguments)}function x(C){return Z.apply(this,arguments)}function Z(){return Z=(0,ae.default)(regeneratorRuntime.mark(function C(_){var g,B;return regeneratorRuntime.wrap(function(R){for(;;)switch(R.prev=R.next){case 0:if(m.default.isEmpty(fe)){R.next=7;break}return R.next=3,te.experimentList.deleteWorkspaceExperiment({workspaceId:fe,workspaceExperimentList:[{experimentId:_}]});case 3:g=R.sent,g.Success&&(L.default.success(s.default.t("Successfully deleted").toString()),V.getExperimentTotals()),R.next=11;break;case 7:return R.next=9,te.experimentList.deleteExperiment({experimentId:_});case 9:B=R.sent,B.Success&&(L.default.success(s.default.t("Successfully deleted").toString()),V.getExperimentTotals());case 11:case"end":return R.stop()}},C)})),Z.apply(this,arguments)}function b(C){return ie.apply(this,arguments)}function ie(){return ie=(0,ae.default)(regeneratorRuntime.mark(function C(_){var g;return regeneratorRuntime.wrap(function(E){for(;;)switch(E.prev=E.next){case 0:return E.next=2,te.experimentList.stopExperiment({taskId:_});case 2:g=E.sent,g.Success&&V.getExperimentTotals();case 4:case"end":return E.stop()}},C)})),ie.apply(this,arguments)}function Pe(C){return Se.apply(this,arguments)}function Se(){return Se=(0,ae.default)(regeneratorRuntime.mark(function C(_){var g,B,E,R,q;return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:if(g="",!fe){W.next=9;break}return W.next=4,te.experimentList.workspaceCloneExperiment(se(se({},_),{},{workspaceId:fe}));case 4:B=W.sent,E=B.Data,g=E,W.next=14;break;case 9:return W.next=11,te.experimentList.cloneExperiment(_);case 11:R=W.sent,q=R.Data,g=q;case 14:g&&(L.default.success(s.default.t("Copy successfully").toString()),V.getExperimentTotals());case 15:case"end":return W.stop()}},C)})),Se.apply(this,arguments)}function Xe(C,_){return Te.apply(this,arguments)}function Te(){return Te=(0,ae.default)(regeneratorRuntime.mark(function C(_,g){var B,E,R;return regeneratorRuntime.wrap(function(J){for(;;)switch(J.prev=J.next){case 0:return J.next=2,te.experimentList.queryExperimentAmount({experimentId:_});case 2:if(B=J.sent,E=B.Data.forecastAmount,!isNaN(E)){J.next=7;break}return A.default.alert({title:s.default.t("Failed to query resource").toString(),content:s.default.t("Please try again").toString(),messageProps:{type:"error"},locale:(0,T.default)().Dialog}),J.abrupt("return");case 7:R=A.default.alert({style:{width:480},title:s.default.t("Start the exercise").toString(),content:ce(g),locale:(0,T.default)().Dialog,footer:t.default.createElement("span",null,t.default.createElement(w.default,{onClick:function(){R.hide()},style:{marginRight:8}},t.default.createElement(M.default,null,"cancel")),t.default.createElement(w.default,{type:"primary",onClick:function(){R.hide(),y(_)}},t.default.createElement(M.default,null,"Confirm"))),onCancel:me});case 8:case"end":return J.stop()}},C)})),Te.apply(this,arguments)}var ce=function(_){var g=m.default.get(_,"experimentAppRisks",[]);return m.default.isEmpty(g)?t.default.createElement("span",{style:{fontSize:12,color:"#555",fontWeight:"normal",lineHeight:"20px"}},t.default.createElement(M.default,null,"After the drill starts, operations such as fault injection will be performed on the specified target (host, etc.), which may make the target (host, etc.) service unavailable. Confirm whether to start the execution")):t.default.createElement("span",{style:{fontSize:12,color:"#555",fontWeight:"normal"}},t.default.createElement("p",null,t.default.createElement(M.default,null,"After the drill starts, operations such as fault injection will be performed on the specified target (host, etc.), which may make the target (host, etc.) service unavailable. Are you sure to start the execution")),t.default.createElement("span",{className:P.default.warnContent},t.default.createElement("p",null,t.default.createElement(M.default,null,"Scenarios included in the walkthrough, the following issues may arise")),t.default.createElement("ul",{className:P.default.tipsContent},m.default.map(g,function(B){return t.default.createElement("li",{className:P.default.startTipsList},t.default.createElement("span",null,B&&B.appName,"\uFF1A"),B&&B.message)}))))},Fe=function(_,g,B){var E=B.experiment,R,q=!1;m.default.isEmpty(E)||(R=E.description,q=E.schedulerConfig&&E.schedulerConfig.cronExpression);var J=E.experimentId,W=E.state,Ie=m.default.get(E.task,"taskId",""),We=t.default.createElement("a",{onClick:function(){W!==K.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?(0,oe.pushUrl)(I,"/chaos/experiment/detail",{id:J}):(0,oe.pushUrl)(I,"/chaos/experiment/task",{id:Ie})},className:P.default.title},q&&t.default.createElement("img",{src:"https://img.alicdn.com/tfs/TB1Ffk2iAcx_u4jSZFlXXXnUFXa-16-16.svg",className:P.default.listIcon,title:s.default.t("Timed task").toString()}),t.default.createElement("span",{className:P.default.displayTitle},_));return t.default.createElement(D.default,{trigger:We,closable:!1},m.default.isEmpty(R)?_:R)},ke=function(_){return t.default.createElement("div",null,!m.default.isEmpty(_)&&m.default.map(_,function(g,B){return t.default.createElement(D.default,{closable:!1,trigger:t.default.createElement(ne.default,{key:"user-experiments-tag-".concat(B),style:{maxWidth:200,marginTop:"2px",marginBottom:"2px"},size:"small",onClick:function(){V.handleTagChange&&V.handleTagChange(g)}},g),key:B},g)}))},Me=function(){var _=arguments.length>0&&arguments[0]!==void 0?arguments[0]:[],g=_.length>ue;return _=_.map(function(B){var E=B.split(/\./g);return E.length>1?E[1]:B}),t.default.createElement("div",{className:P.default.apps},!m.default.isEmpty(_)&&m.default.map(g?m.default.slice(_,0,ue):_,function(B,E){var R=m.default.upperFirst(B).charAt(0),q=De[R.charCodeAt(0)-65];return t.default.createElement(D.default,{key:"user-experiments-app-".concat(E),trigger:t.default.createElement("div",{className:P.default.app,style:{backgroundColor:q}},R),closable:!1},m.default.upperCase(B))}),g?t.default.createElement(D.default,{trigger:t.default.createElement("div",{className:P.default.app,style:{backgroundColor:"#E5E5E5"}},"..."),closable:!1},m.default.join(m.default.map(m.default.slice(_,ue),m.default.upperCase),",")):"")},ve=function(_,g,B){var E=B.experiment,R=E.taskState,q=E.taskResult,J,W="";return E.state===K.ExperimentConstants.EXPERIMENT_STATE_DRAFT?W="- - -":E.state===K.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?(J=t.default.createElement(S.default,{type:"loading",size:"small",style:{marginRight:5}}),R===K.ExperimentConstants.EXPERIMENT_TASK_STATE_STOPPING?W=s.default.t("Stopping"):E.taskUserCheckState===K.ExperimentConstants.EXPERIMENT_ACTIVITY_TASK_USER_CHECK_STATE_WAITING?W=s.default.t("Wait for user confirmation"):W=s.default.t("In progress")):R===K.ExperimentConstants.EXPERIMENT_TASK_STATE_FINISHED&&(q===K.ExperimentConstants.EXPERIMENT_TASK_RESULT_SUCCESS&&(J=t.default.createElement(S.default,{type:"success",style:{color:"#1F8E3D",marginRight:6},size:"small"}),W=s.default.t("Success")),q===K.ExperimentConstants.EXPERIMENT_TASK_RESULT_FAILED&&(J=t.default.createElement(S.default,{type:"warning",style:{color:"#D93026",marginRight:6},size:"small"}),W=s.default.t("Not as expected")),q===K.ExperimentConstants.EXPERIMENT_TASK_RESULT_ERROR&&(J=t.default.createElement(S.default,{type:"warning",style:{color:"#D93026",marginRight:6},size:"small"}),W=s.default.t("Abnormal")),(q===K.ExperimentConstants.EXPERIMENT_TASK_RESULT_STOPPED||E.taskResult===K.ExperimentConstants.EXPERIMENT_TASK_RESULT_REJECTED)&&(J=t.default.createElement(S.default,{type:"minus-circle-fill",style:{color:"#FFC440",marginRight:6},size:"small"}),W=s.default.t("Interrupt"))),t.default.createElement("div",null,J,t.default.createElement("span",null,W))},je=function(_){return _.map(function(g,B){return t.default.createElement("span",{key:g.name},g.name,_.length-1!==B&&"\u3001")})};function Ye(C,_){return Ue.apply(this,arguments)}function Ue(){return Ue=(0,ae.default)(regeneratorRuntime.mark(function C(_,g){var B,E,R,q;return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:if(B=[],!m.default.isEmpty(fe)){W.next=7;break}return W.next=4,te.experimentList.getWorkspaceByExperimentId({experimentId:g});case 4:E=W.sent,R=E.Data,B=R;case 7:q=A.default.alert({title:s.default.t("Warn").toString(),content:t.default.createElement("div",{className:P.default.deleteTips},fe&&"".concat(s.default.t("Remove walkthrough"),"\uFF08").concat(_,",").concat(s.default.t("will remove it from this space"),"\uFF08").concat(V.workspaceName,"\uFF09").concat(s.default.t("connection relation. OK to remove")),!fe&&t.default.createElement("div",null,m.default.isEmpty(B)?t.default.createElement("div",null," ",s.default.t("The walkthrough").toString(),"\uFF08",_,"\uFF09",s.default.t("It only exists in my space, are you sure you want to delete it?").toString()):t.default.createElement("div",null,s.default.t("Delete walkthrough"),"\uFF08",_,"\uFF09\uFF0C",s.default.t("Will delete all associated spaces"),"\uFF08",je(B)," ",s.default.t("This walkthrough in , OK to delete"),"\uFF1F"))),footer:t.default.createElement(t.default.Fragment,null,t.default.createElement(w.default,{type:"primary",onClick:function(){x(g),q.hide()}},t.default.createElement(M.default,null,"Confirm")),t.default.createElement(w.default,{onClick:function(){return q.hide()}}," ",t.default.createElement(M.default,null,"cancel"))),locale:(0,T.default)().Dialog});case 8:case"end":return W.stop()}},C)})),Ue.apply(this,arguments)}function Oe(C,_,g){return t.default.createElement(w.default,{className:(0,c.default)(P.default.opt,(0,a.handleIsAdmin)(g,2)?P.default.warning:"",(0,a.handleIsAdmin)(g,2)?"":P.default.disable),disabled:!(0,a.handleIsAdmin)(g,2),style:{marginLeft:0},text:!0,onClick:function(E){E.stopPropagation(),Ye(C,_)},type:"primary"},fe?s.default.t("Remove").toString():s.default.t("Delete").toString())}function ze(C){var _=m.default.get(C,"blockReasons",[]);return t.default.createElement("ul",{className:P.default.ulList},(0,H.default)(_).map(function(g){return t.default.createElement("li",{key:g,className:P.default.baoollnList},g)}))}function $e(C){var _=C.experimentId,g=C.state,B=C.name,E=C.opLevel,R=C.taskState,q=C.permission,J=q===void 0?0:q,W=m.default.get(C,"task.taskId","");return E?t.default.createElement(t.default.Fragment,null,t.default.createElement(D.default,{trigger:t.default.createElement(S.default,{type:"help",size:"xs",className:P.default.helpIcon}),closable:!1},ze(C)),t.default.createElement(w.default,{disabled:E!==1||!(0,a.handleIsAdmin)(J,4),className:P.default.opt,text:!0,type:"primary"},t.default.createElement(M.default,null,"Drill"))):g!==K.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?g===K.ExperimentConstants.EXPERIMENT_STATE_SYNC?t.default.createElement(D.default,{trigger:t.default.createElement("span",{className:(0,c.default)(P.default.opt,P.default.disable)},"  ",t.default.createElement(M.default,null,"Drill")),closable:!1},t.default.createElement(M.default,null,"Run after editing")):t.default.createElement(w.default,{disabled:!(0,a.handleIsAdmin)(J,4),className:P.default.opt,text:!0,onClick:function(){return Xe(_,C)},type:"primary"},t.default.createElement(M.default,null,"Drill")):R===K.ExperimentConstants.EXPERIMENT_TASK_STATE_STOPPING?t.default.createElement(D.default,{trigger:t.default.createElement("span",{className:(0,c.default)(P.default.opt,P.default.disable)}," ",t.default.createElement(M.default,null,"Stop")),closable:!1},t.default.createElement(M.default,null,"Rehearsal is in progress")):t.default.createElement(w.default,{className:(0,c.default)(P.default.opt,(0,a.handleIsAdmin)(J,4)&&P.default.warning),disabled:!(0,a.handleIsAdmin)(J,4),text:!0,onClick:function(){A.default.confirm({title:s.default.t("Stop the drill").toString(),content:"".concat(s.default.t("Are you sure to stop the exercise [drill name"),": ").concat(B,"]\uFF1F"),locale:(0,T.default)().Dialog,onOk:function(){return b(W)}})}},t.default.createElement(M.default,null,"Stop"))}var we=function(_){var g=_.experimentId,B=_.state,E=_.name,R=_.permission,q=R===void 0?0:R;return t.default.createElement("div",{className:P.default.optGroup},$e(_),t.default.createElement("span",{style:{marginRight:8,color:"#d8d8d8"}},"|"),t.default.createElement(ee.default,{experiment:_,disable:!(0,a.handleIsAdmin)(q,2),onSubmit:Pe}),t.default.createElement("span",{style:{marginRight:8,color:"#d8d8d8"}},"|"),B!==K.ExperimentConstants.EXPERIMENT_TASK_STATE_RUNNING?Oe(E,g,q):t.default.createElement(D.default,{trigger:t.default.createElement("span",{className:(0,c.default)(P.default.opt,P.default.disable)},fe?s.default.t("Remove").toString():s.default.t("Delete").toString()),closable:!1},t.default.createElement(M.default,null,"On-going drills are prohibited from deletion")))};return t.default.createElement("div",{className:P.default.container},t.default.createElement(Y.default,{primaryKey:"experimentId",hasBorder:!1,dataSource:!V.running&&le||!(0,a.handleIsAdmin)(V.permission,1)?[]:ye,loading:V.running?!1:le,onFilter:function(){return console.log(1)},locale:(0,T.default)().Table},t.default.createElement(_e,{title:s.default.t("Drill name").toString(),dataIndex:"experiment.name",cell:Fe,width:"16%"}),t.default.createElement(_e,{title:s.default.t("Tag").toString(),dataIndex:"experiment.tags",cell:ke,width:"16%"}),t.default.createElement(_e,{title:s.default.t("Scenes").toString(),dataIndex:"experiment.miniAppDesc",cell:Me}),t.default.createElement(_e,{title:s.default.t("Create Time").toString(),dataIndex:"experiment.createTime",width:"13%"}),t.default.createElement(_e,{title:s.default.t("Timed task").toString(),dataIndex:"experiment.schedulerConfig.cronExpression"}),t.default.createElement(_e,{title:s.default.t("Latest running status").toString(),dataIndex:"experiment.state",cell:ve,width:"9%"}),t.default.createElement(_e,{title:s.default.t("Last running time").toString(),dataIndex:"experiment.taskStartTime",width:"13%"}),t.default.createElement(_e,{title:s.default.t("Operation").toString(),width:"12%",lock:"right",dataIndex:"experiment",cell:we})),t.default.createElement(z.default,{className:P.default.homePagination,shape:"arrow-only",pageSizePosition:"start",current:V.page,total:Ne,locale:(0,T.default)().Pagination,onChange:V.handlePageChange,hideOnlyOnePage:!0}))},Be=Ce;O.default=Be})},25300:function(k,f,e){var r,h,d,p=e(67394);(function(i,n){if(!0)!(h=[f,e(12955),e(83086),e(27378),e(14798),e(68055),e(88972)],r=n,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(i,n,l,o,u,O,H){"use strict";var L=e(67971);p(i,"__esModule",{value:!0}),i.default=void 0,n=L(n),l=L(l),o=L(o),u=L(u),O=L(O),H=L(H);var z=function(S){return o.default.createElement(n.default,{title:u.default.t("New drill").toString(),visible:S.visible,onClose:S.handleClose,className:H.default.DialogExperience,footer:!1,locale:(0,O.default)().Dialog},o.default.createElement("div",{className:H.default.warp},o.default.createElement(l.default,{onEmpty:S.onEmpty,hideEmpty:S.hideEmpty,noFooter:!0,onChose:S.handleChoseCreate})))},A=z;i.default=A})},45919:function(k,f,e){var r,h,d,p=e(67394);(function(i,n){if(!0)!(h=[f,e(17225),e(27378),e(66697),e(65328)],r=n,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(i,n,l,o,u){"use strict";var O=e(67971);p(i,"__esModule",{value:!0}),i.default=void 0,n=O(n),l=O(l),o=O(o),u=O(u);var H=function(A){var w=A.statisitcInfo,S=w===void 0?{}:w,ne=S.total,D=S.active,ae=S.running,j=S.failure,Y=S.success,ee=S.idle;function t(){return l.default.createElement("svg",{width:"70px",height:"18px",viewBox:"0 0 70 18",version:"1.1",xmlns:"http://www.w3.org/2000/svg",xmlnsXlink:"http://www.w3.org/1999/xlink"},l.default.createElement("g",{id:"Page-1",stroke:"none",strokeWidth:"1",fill:"none",fillRule:"evenodd"},l.default.createElement("g",{id:"\u6545\u969C\u6F14\u7EC3\u5217\u8868",transform:"translate(-639.000000, -154.000000)"},l.default.createElement("g",{id:"\u6F14\u7EC3\u72B6\u6001",transform:"translate(639.000000, 154.000000)"},l.default.createElement("path",{d:"M-1.42108547e-14,0 L70,0 L65.813729,15.0705755 C65.3328182,16.8018545 63.7564886,18 61.9596574,18 L8.0403426,18 C6.24351138,18 4.66718181,16.8018545 4.18627096,15.0705755 L-1.42108547e-14,0 L-1.42108547e-14,0 Z",id:"Rectangle",fill:"#A5C3DE"}),l.default.createElement("text",{fontFamily:"PingFangSC-Regular, PingFang SC",fontSize:"12",fontWeight:"normal",fill:"#FFFFFF"},l.default.createElement("tspan",{x:"11",y:"14"},l.default.createElement(o.default,null,"Drill status")))))))}return l.default.createElement("div",{className:u.default.wrapper},l.default.createElement("div",{className:u.default.statisticDone},l.default.createElement("div",{className:u.default.doneBox},l.default.createElement("div",{className:u.default.title},l.default.createElement(o.default,null,"Exercises performed")),l.default.createElement("span",{className:u.default.number},D)),l.default.createElement("div",{className:u.default.separator}),l.default.createElement("div",{className:u.default.detailBox},l.default.createElement("div",{className:u.default.topTip},t()),l.default.createElement("div",{className:u.default.failedBox},l.default.createElement(n.default,{type:"exclamation-circle",className:u.default.icon,size:"small"}),l.default.createElement("span",null,l.default.createElement(o.default,null,"Fail"),":"),l.default.createElement("span",{className:u.default.detailFont},j)),l.default.createElement("div",{className:u.default.runningBox},l.default.createElement(n.default,{type:"clock",className:u.default.icon,size:"small"}),l.default.createElement("span",null,l.default.createElement(o.default,null,"Running"),":"),l.default.createElement("span",{className:u.default.detailFont},ae)),l.default.createElement("div",{className:u.default.successBox},l.default.createElement(n.default,{type:"check-circle",className:u.default.icon,size:"small"}),l.default.createElement("span",null,l.default.createElement(o.default,null,"Success"),":"),l.default.createElement("span",{className:u.default.detailFont},Y)))),l.default.createElement("div",{className:u.default.statisticUnDone},l.default.createElement("div",{className:u.default.title},l.default.createElement(o.default,null,"Exercises not performed")),l.default.createElement("span",{className:u.default.number},ee)),l.default.createElement("div",{className:u.default.statisticTotal},l.default.createElement("div",{className:u.default.title},l.default.createElement(o.default,null,"General drill total")),l.default.createElement("span",{className:u.default.number},ne)))},L=H;i.default=L})},49789:function(k,f,e){var r,h,d,p=e(24596),i=e(67394),n=e(93168),l=e(23587),o=e(83452),u=e(95315),O=e(63774),H=e(92937);(function(L,z){if(!0)!(h=[f,e(75453),e(32186),e(83452),e(79566),e(73915),e(94188),e(28757),e(12955),e(34132),e(85645),e(72153),e(17225),e(17534),e(35049),e(57379),e(77809),e(81853),e(95001),e(2484),e(25300),e(27378),e(45919),e(58184),e(66697),e(98784),e(14798),e(68055),e(18390),e(96291),e(99328),e(70343),e(14870),e(42058),e(49729)],r=z,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var A})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(L,z,A,w,S,ne,D,ae,j,Y,ee,t,M,m,c,s,T,v,P,K,oe,a,pe,ge,N,de,se,_e,ue,De,me,Ce,Be,Ee,V){"use strict";var I=e(67971);i(L,"__esModule",{value:!0}),L.default=void 0,z=I(z),A=I(A),w=I(w),S=I(S),ne=I(ne),D=I(D),ae=I(ae),j=I(j),Y=I(Y),ee=I(ee),t=I(t),M=I(M),m=I(m),c=I(c),s=I(s),T=I(T),v=I(v),P=I(P),K=I(K),oe=I(oe),a=fe(a),pe=I(pe),ge=I(ge),N=I(N),de=I(de),se=I(se),_e=I(_e),ue=I(ue);function te(y){if(typeof n!="function")return null;var $=new n,x=new n;return(te=function(b){return b?x:$})(y)}function fe(y,$){if(!$&&y&&y.__esModule)return y;if(y===null||p(y)!=="object"&&typeof y!="function")return{default:y};var x=te($);if(x&&x.has(y))return x.get(y);var Z={},b=i&&l;for(var ie in y)if(ie!=="default"&&Object.prototype.hasOwnProperty.call(y,ie)){var Pe=b?l(y,ie):null;Pe&&(Pe.get||Pe.set)?i(Z,ie,Pe):Z[ie]=y[ie]}return Z.default=y,x&&x.set(y,Z),Z}function Re(y,$){var x=o(y);if(u){var Z=u(y);$&&(Z=Z.filter(function(b){return l(y,b).enumerable})),x.push.apply(x,Z)}return x}function ye(y){for(var $=1;$<arguments.length;$++){var x=arguments[$]!=null?arguments[$]:{};$%2?Re(Object(x),!0).forEach(function(Z){(0,s.default)(y,Z,x[Z])}):O?H(y,O(x)):Re(Object(x)).forEach(function(Z){i(y,Z,l(x,Z))})}return y}var Ne=function($){var x=(0,Be.useDispatch)(),Z=(0,Ee.useHistory)(),b=(0,me.getParams)("workspaceId"),ie=(0,me.getParams)("_st"),Pe=(0,Be.useSelector)(function(X){return{loading:X.loading.effects["experimentList/getExperimentList"],chaosContext:X.loginUser}}),Se=Pe.chaosContext,Xe=(0,a.useState)(),Te=(0,v.default)(Xe,2),ce=Te[0],Fe=Te[1],ke=(0,a.useState)(""),Me=(0,v.default)(ke,2),ve=Me[0],je=Me[1],Ye=(0,a.useState)((ie==null?void 0:ie.split(","))||[]),Ue=(0,v.default)(Ye,2),Oe=Ue[0],ze=Ue[1],$e=(0,a.useState)((ie==null?void 0:ie.split(","))||[]),we=(0,v.default)($e,2),C=we[0],_=we[1],g=(0,a.useState)(1),B=(0,v.default)(g,2),E=B[0],R=B[1],q=(0,a.useState)([]),J=(0,v.default)(q,2),W=J[0],Ie=J[1],We=(0,a.useState)([]),Ze=(0,v.default)(We,2),he=Ze[0],He=Ze[1],Et=(0,a.useState)(!1),et=(0,v.default)(Et,2),Ge=et[0],tt=et[1],pt=(0,a.useState)(!1),nt=(0,v.default)(pt,2),Je=nt[0],at=nt[1],mt=(0,a.useState)(!1),lt=(0,v.default)(mt,2),it=lt[0],ct=lt[1],gt=(0,a.useState)(7),rt=(0,v.default)(gt,2),ht=rt[0],st=rt[1],xt=(0,a.useState)(!1),dt=(0,v.default)(xt,2),Ct=dt[0],Bt=dt[1],Rt=(0,a.useState)(!1),ot=(0,v.default)(Rt,2),Le=ot[0],Pt=ot[1],It=(0,a.useState)(!1),ut=(0,v.default)(It,2),Ke=ut[0],St=ut[1],vt=(0,a.useState)(!1),At=(0,v.default)(vt,2),yt=At[0],Tt=At[1];(0,a.useEffect)(function(){x.pageHeader.setTitle(se.default.t("Walkthrough List").toString()),x.pageHeader.setBreadCrumbItems(De.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"experimentlist",value:se.default.t("Walkthrough List").toString(),path:"/chaos"}]))},[]),(0,a.useEffect)(function(){(0,T.default)(regeneratorRuntime.mark(function X(){return regeneratorRuntime.wrap(function(U){for(;;)switch(U.prev=U.next){case 0:return U.next=2,x.expertises.getExpertiseBase({page:1,size:10});case 2:case"end":return U.stop()}},X)}))()},[]),(0,a.useEffect)(function(){(0,T.default)(regeneratorRuntime.mark(function X(){var F,U,Q,re;return regeneratorRuntime.wrap(function(G){for(;;)switch(G.prev=G.next){case 0:if(de.default.isEmpty(b)){G.next=8;break}return G.next=3,x.experimentList.getGeneralWorkSpaceStatInfo({workspaceId:b});case 3:F=G.sent,U=F.Data,Fe(U),G.next=13;break;case 8:return G.next=10,x.experimentList.getExperimentTaskStatistic();case 10:Q=G.sent,re=Q.Data,Fe(re);case 13:case"end":return G.stop()}},X)}))()},[Ge,b]),(0,a.useEffect)(function(){var X=_t(),F=X.status,U=X.results;(0,T.default)(regeneratorRuntime.mark(function Q(){var re,xe,G;return regeneratorRuntime.wrap(function(Ae){for(;;)switch(Ae.prev=Ae.next){case 0:if(!(!de.default.isEmpty(b)&&!de.default.isEmpty(Se))){Ae.next=10;break}return re={searchKey:ve,states:F,results:U,page:E,size:10,tagNames:he,workspaceId:b},xe=Se.userId,Le&&(re.filterUserId=xe),Ae.next=6,x.experimentList.getPageableGeneralExperiments(ye({},re));case 6:G=Ae.sent,st(G),Ae.next=12;break;case 10:return Ae.next=12,x.experimentList.getExperimentList({searchKey:ve,states:F,results:U,page:E,size:10,tagNames:he,scheduler:Ke});case 12:case"end":return Ae.stop()}},Q)}))()},[ve,C,E,he,b,Ct,Le,Ke]),(0,V.useInterval)(function(){ce&&ce.running&&be()},ce&&ce.running?5e3:null);var _t=function(){if(C.length===0)return{status:[],results:[]};var F=[],U=[];return C.map(function(Q){var re=Ce.SearchOptDict[Q]||{},xe=re.status,G=re.results;return F.push(xe),U=[].concat((0,c.default)(U),(0,c.default)(G)),Q}),{status:(0,z.default)(new A.default(F)),results:(0,z.default)(new A.default(U))}};function be(){return Qe.apply(this,arguments)}function Qe(){return Qe=(0,T.default)(regeneratorRuntime.mark(function X(){var F,U,Q,re,xe,G;return regeneratorRuntime.wrap(function(Ae){for(;;)switch(Ae.prev=Ae.next){case 0:if(F=_t(),U=F.status,Q=F.results,de.default.isEmpty(b)){Ae.next=12;break}return re={searchKey:ve,states:U||[],results:Q,page:E,size:10,tagNames:he,workspaceId:b},xe=Se.userId,Le&&(re.filterUserId=xe),Ae.next=7,x.experimentList.getPageableGeneralExperiments(ye({},re));case 7:G=Ae.sent,st(G),tt(!Ge),Ae.next=15;break;case 12:return Ae.next=14,x.experimentList.getExperimentList({searchKey:ve,states:U,results:Q,page:E,size:10,tagNames:he,scheduler:Ke});case 14:tt(!Ge);case 15:case"end":return Ae.stop()}},X)})),Qe.apply(this,arguments)}function Ut(){return Ve.apply(this,arguments)}function Ve(){return Ve=(0,T.default)(regeneratorRuntime.mark(function X(){var F,U,Q,re;return regeneratorRuntime.wrap(function(G){for(;;)switch(G.prev=G.next){case 0:if(de.default.isEmpty(b)){G.next=8;break}return G.next=3,x.experimentList.listGeneralWorkspaceExperimentTags({workspaceId:b});case 3:F=G.sent,U=F.Data,de.default.isEmpty(U)||Ie(U),G.next=13;break;case 8:return G.next=10,x.experimentList.getListExperimentTags();case 10:Q=G.sent,re=Q.Data,de.default.isEmpty(re)||Ie(re);case 13:case"end":return G.stop()}},X)})),Ve.apply(this,arguments)}(0,a.useEffect)(function(){_((ie==null?void 0:ie.split(","))||[]),R(1)},[ie]);function Dt(){(0,me.pushUrl)(Z,"/chaos/workspace/owner",{_st:Oe.join(",")})}function Nt(X){je(X),R(1)}function Ft(X){R(X)}function Mt(X){He(X),R(1)}function Ot(){return qe.apply(this,arguments)}function qe(){return qe=(0,T.default)(regeneratorRuntime.mark(function X(){var F;return regeneratorRuntime.wrap(function(Q){for(;;)switch(Q.prev=Q.next){case 0:return Q.next=2,x.experimentList.stopAllExperimentTasks();case 2:F=Q.sent,F.Success&&be();case 4:case"end":return Q.stop()}},X)})),qe.apply(this,arguments)}function wt(){x.experimentEditor.setClearExperiment(),at(!Je)}function Wt(){(0,me.pushUrl)(Z,"/chaos/experiment/editor")}function Lt(X){var F=X.expertise_id,U=de.default.get(X,"scope_type",[]),Q=[];U.forEach(function(re){re===0&&Q.push(se.default.t("Host")),re===2&&Q.push(se.default.t("Kubernetes"))}),m.default.show({type:"notice",title:a.default.createElement("div",null,"".concat(se.default.t("Supported by current experience")).concat(Q.join(",")).concat(se.default.t("Application type")))}),(0,me.pushUrl)(Z,"/chaos/experiment/editor",{expertiseId:F})}function Kt(){(0,me.pushUrl)(Z,"/chaos/freshapplication/access")}var bt=function(F){x.experimentEditor.setClearExperiment(),F==="-1"?at(!Je):(0,me.pushUrl)(Z,F)};function Xt(){var X=ce&&ce.running;return a.default.createElement("div",{className:ue.default.operations},a.default.createElement("div",{style:{display:"flex"}},a.default.createElement(Y.default,{onVisibleChange:function(U){return Tt(U)},trigger:a.default.createElement(t.default,{className:ue.default.createButton,type:"primary"},a.default.createElement(N.default,null,"New drill")," ",a.default.createElement(M.default,{type:yt?"angle-down":"angle-right"}))},a.default.createElement(ee.default,{className:ue.default.createMenu,onItemClick:function(U){return bt(U)}},a.default.createElement(ee.default.Item,{key:"/chaos/experiment/editor"},a.default.createElement(N.default,null,"New Blank Walkthrough")),a.default.createElement(ee.default.Item,{key:"-1"},a.default.createElement(N.default,null,"New from experience base")))),b&&a.default.createElement(t.default,{style:{marginLeft:8},onClick:ft},a.default.createElement(N.default,null,"Add walkthrough")),a.default.createElement(t.default,{className:ue.default.stopButton,warning:!0,disabled:X===0,onClick:function(){j.default.confirm({title:se.default.t("Stop all drills").toString(),content:"".concat(se.default.t("Currently has")).concat(X).concat(se.default.t("Drills are in progress, are they all stopped")),locale:(0,_e.default)().Dialog,onOk:function(){return Ot()}})}},a.default.createElement(N.default,null,"Stop all")),a.default.createElement(ae.default,{placeholder:se.default.t("Please select a status").toString(),className:ue.default.select,mode:"multiple",tagInline:!0,maxTagPlaceholder:function(){return""},onChange:function(U){return ze(U)},onBlur:Dt,value:Oe,locale:(0,_e.default)().Select},(0,w.default)(Ce.SearchOptDict).map(function(F){var U=Ce.SearchOptDict[F]||{},Q=U.name;return a.default.createElement(ae.default.Option,{key:F,value:F},Q)})),a.default.createElement(ge.default,{data:W,onSubmit:Mt,tagNames:he,onFocus:Ut}),a.default.createElement(D.default,{shape:"simple",className:ue.default.search,placeholder:"\u8BF7\u8F93\u5165\u6F14\u7EC3\u540D\u79F0",onChange:Nt}),b&&a.default.createElement("div",{className:ue.default.switch},a.default.createElement("span",null,a.default.createElement(N.default,null,"Show only your own walkthrough")),a.default.createElement(ne.default,{size:"small",checked:Le,onChange:function(U){return Pt(U)}})),a.default.createElement(S.default,{className:ue.default.schedulerCb,checked:Ke,onChange:function(U){return St(U)}},a.default.createElement(N.default,null,"Just watch timed drills"))),a.default.createElement("span",null,a.default.createElement(t.default,{type:"primary",text:!0,onClick:Kt},a.default.createElement(N.default,null,"Application access"))))}function ft(){ct(!it)}function kt(){Bt(!0)}function jt(X){if(!he.includes(X))He([].concat((0,c.default)(he),[X]));else{var F=de.default.pull(he,X);He((0,c.default)(F))}}return a.default.createElement("div",{style:{marginBottom:16}},a.default.createElement(pe.default,{statisitcInfo:ce}),Xt(),a.default.createElement(K.default,{workspaceName:$.workspaceName,running:ce&&ce.running,permission:ht,handlePageChange:Ft,handleTagChange:jt,page:E,getExperimentTotals:be,getExperienceBag:kt}),a.default.createElement(oe.default,{visible:Je,hideEmpty:!0,handleClose:wt,onEmpty:Wt,handleChoseCreate:Lt}),a.default.createElement(P.default,{getExperimentTotals:be,visible:it,onCancel:ft}))},le=Ne;L.default=le})},87344:function(k,f,e){var r,h,d,p=e(24596),i=e(67394),n=e(93168),l=e(23587);(function(o,u){if(!0)!(h=[f,e(49789),e(27378),e(14798),e(96291),e(99328),e(14870)],r=u,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var O})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(o,u,O,H,L,z,A){"use strict";var w=e(67971);i(o,"__esModule",{value:!0}),o.default=void 0,u=w(u),O=ne(O),H=w(H);function S(j){if(typeof n!="function")return null;var Y=new n,ee=new n;return(S=function(M){return M?ee:Y})(j)}function ne(j,Y){if(!Y&&j&&j.__esModule)return j;if(j===null||p(j)!=="object"&&typeof j!="function")return{default:j};var ee=S(Y);if(ee&&ee.has(j))return ee.get(j);var t={},M=i&&l;for(var m in j)if(m!=="default"&&Object.prototype.hasOwnProperty.call(j,m)){var c=M?l(j,m):null;c&&(c.get||c.set)?i(t,m,c):t[m]=j[m]}return t.default=j,ee&&ee.set(j,t),t}var D=function(){var Y=(0,A.useDispatch)();return(0,O.useEffect)(function(){Y.pageHeader.setTitle(H.default.t("My spac").toString()),Y.pageHeader.setBreadCrumbItems(L.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"workspace_detail",value:H.default.t("My spac").toString(),path:""}]))}),(0,O.useEffect)(function(){(0,z.removeParams)("id"),(0,z.removeParams)("permission"),(0,z.removeParams)("expertiseId")},[]),O.default.createElement(u.default,{workspaceName:""})},ae=D;o.default=ae})},17640:function(k,f,e){var r,h,d,p=e(67394);(function(i,n){if(!0)!(h=[f],r=n,d=typeof r=="function"?r.apply(f,h):r,d!==void 0&&(k.exports=d));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(i){"use strict";p(i,"__esModule",{value:!0}),i.handleIsAdmin=void 0;var n=function(o,u){return(o&u)===u};i.handleIsAdmin=n})},93705:(k,f,e)=>{"use strict";e.d(f,{Z:()=>n});var r=e(60994),h=e.n(r),d=e(93476),p=e.n(d),i=p()(h());i.push([k.id,`.index__warp__QeSbE {
  width: 500px;
}
  .index__warp__QeSbE .index__top__dMmY3 {
    height: 36px;
    background: #f7f7f7;
    color: #555555;
    line-height: 5px;
    padding: 16px;
    margin-bottom: 16px;
    font-size: 12px;
  }
  .index__warp__QeSbE .index__item__KFeZ3 {
    display: flex;
  }
  .index__warp__QeSbE .index__item__KFeZ3 .index__left__H07-S {
      font-size: 12px;
      color: #555555;
      line-height: 32px;
      width: 20%;
    }
  .index__warp__QeSbE .index__select__WAuqz {
    width: 80%;
  }

.index__popup__6bVA1 .next-menu-item-inner {
    height: 60px;
  }

.index__popup__6bVA1 .index__workspaceName__WP\\+ov {
    font-size: 12px;
    color: #888888;
  }
`,"",{version:3,sources:["webpack://./pages/Chaos/ExperimentList/AddExperiment/index.css"],names:[],mappings:"AAAA;EACE,YAAY;AAsBd;EArBE;IACE,YAAY;IACZ,mBAAmB;IACnB,cAAc;IACd,gBAAgB;IAChB,aAAa;IACb,mBAAmB;IACnB,eAAe;EACjB;EACA;IACE,aAAa;EAOf;EANE;MACE,eAAe;MACf,cAAc;MACd,iBAAiB;MACjB,UAAU;IACZ;EAEF;IACE,UAAU;EACZ;;AAIA;IACE,YAAY;EACd;;AACA;IACE,eAAe;IACf,cAAc;EAChB",sourcesContent:[`.warp {
  width: 500px;
  .top {
    height: 36px;
    background: #f7f7f7;
    color: #555555;
    line-height: 5px;
    padding: 16px;
    margin-bottom: 16px;
    font-size: 12px;
  }
  .item {
    display: flex;
    .left {
      font-size: 12px;
      color: #555555;
      line-height: 32px;
      width: 20%;
    }
  }
  .select {
    width: 80%;
  }
}

.popup {
  :global(.next-menu-item-inner) {
    height: 60px;
  }
  .workspaceName {
    font-size: 12px;
    color: #888888;
  }
}
`],sourceRoot:""}]),i.locals={warp:"index__warp__QeSbE",top:"index__top__dMmY3",item:"index__item__KFeZ3",left:"index__left__H07-S",select:"index__select__WAuqz",popup:"index__popup__6bVA1",workspaceName:"index__workspaceName__WP+ov"};const n=i},46812:(k,f,e)=>{"use strict";e.d(f,{Z:()=>n});var r=e(60994),h=e.n(r),d=e(93476),p=e.n(d),i=p()(h());i.push([k.id,`.index__container__5pnJG tr, .index__container__5pnJG td {
    padding: 0 !important;
    margin: 0 !important;
    height: 50px;
  }
  /* :global(.next-table-cell-wrapper) {
    font-size: 14px !important;
  } */
  .index__container__5pnJG tr > th > .next-table-cell-wrapper {
    padding: 0 0 0 16px !important;
    /* font-size: 14px !important;
    font-weight: bold !important; */
    height: 52px !important;
    line-height: 52px !important;
    background-color: #fafafa;
  }
  .index__container__5pnJG tr > td > .next-table-cell-wrapper {
    padding: 0 0 0 16px !important;
  }
  .index__container__5pnJG tr td:first-child:hover {
    cursor: pointer;
  }
  .index__container__5pnJG a {
    text-decoration: none;
  }
  .index__container__5pnJG title {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    height: $rowHeight;
    line-height: 22px;
  }
  .index__container__5pnJG .index__listIcon__5VGEE {
      position: relative;
      bottom: 0;
      margin-right: 3px;
    }
  .index__container__5pnJG .index__title__utO2v:hover {
    text-decoration: none;
    opacity: .8;
  }
  .index__container__5pnJG .index__displayTitle__LuH1p {
    /* display: inline-block; */
    color: #0070CC;
  }
  .index__container__5pnJG .index__apps__Km2AZ {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
  }
  .index__container__5pnJG .index__app__QY2yH {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    font-family: PingFangSC-Medium;
    color: #FFFFFF;
    margin-right: 5px;
    cursor: pointer;
  }
  .index__container__5pnJG .index__homePagination__\\+jylf {
    text-align: right;
    margin-top: 16px;
    margin-right: 16px;
    margin-bottom: 30px;
  }
  .index__container__5pnJG .index__optGroup__PlYxl {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: center;
  }
  .index__container__5pnJG .index__opt__XATIF {
    /* font-family: PingFangSC-Regular; */
    /* font-size: 14px; */
    /* color: #0066CC; */
    /* letter-spacing: 0; */
    /* cursor: pointer; */
    margin-right: 8px;
  }
  /* &.disable {
      color: #BFBFBF;
      cursor: not-allowed;
    } */
  .index__container__5pnJG .index__opt__XATIF.index__warning__OIkiA {
      color: #FF4F4C;
    }
  .index__container__5pnJG .index__opt__XATIF:first-child {
    margin-left: 0;
  }

.index__cloneTitle__1xIZC {
  opacity: 0.65;
  font-size: 14px;
  color: #000000;
  letter-spacing: 0;
}

.index__cloneBtnRow__8k53- {
  margin-top: 8px;
}

.index__cloneBtnRow__8k53- .next-btn {
    padding: 0;
    width: 48px;
    height: 24px;
    text-align: center;
    line-height: 24px;
  }

.index__cloneBtnRow__8k53- .next-btn-normal {
    margin-left: 8px;
  }

.index__helpIcon__KbxL0 {
  color: #D93026;
  cursor: pointer;
  margin-right: 4px;
}

.index__optYellow__ZlCap {
  font-family: PingFangSC-Regular;
  font-size: 14px;
  color: #0066CC;
  letter-spacing: 0;
  cursor: pointer;
}

.index__disable__FXeNR {
  color: #BFBFBF;
  cursor: not-allowed;
}

.index__ulList__ebeQV {
  list-style-type: square;
  padding-left: 10px;
}

.index__ulList__ebeQV .index__baoollnList__Z4HhB {
    line-height: 32px;
  }

.index__deleteTips__wj5Ms {
  width: 400px;
  line-height: 24px;
}

/* \u6F14\u7EC3\u8D44\u6E90\u5305\u90E8\u5206 */
.index__paidTips__jUmR3 {
  font-size: 12px;
  color: #333;
}

.index__packName__aWYnq {
  display: inline-block;
  padding: 2px 2px;
  height: 16px;
  width: 44px;
  background: #0091FF;
  border-radius: 2px;
  background-color: #0091FF;
  font-family: PingFangSC-Regular;
  color: #fff;
}

.index__action__bBvr7 {
  color: #0070CC;
  cursor: pointer;
}

.index__tipsContent__OPmgX {
  padding-left: 20px;
  list-style: disc;
}

.index__tipsContent__OPmgX .index__startTipsList__IZGyD {
    min-height: 22px;
    line-height: 22px;
  }

.index__warnContent__I1koO {
  display: inline-block;
  width: 100%;
  padding: 15px;
  background: #FFF7DB;
}`,"",{version:3,sources:["webpack://./pages/Chaos/ExperimentList/Experiments/index.css"],names:[],mappings:"AACE;IACE,qBAAqB;IACrB,oBAAoB;IACpB,YAAY;EACd;EAEA;;KAEG;EAEH;IACE,8BAA8B;IAC9B;mCAC+B;IAC/B,uBAAuB;IACvB,4BAA4B;IAC5B,yBAAyB;EAC3B;EAEA;IACE,8BAA8B;EAChC;EAEA;IACE,eAAe;EACjB;EAEA;IACE,qBAAqB;EACvB;EAEA;IACE,aAAa;IACb,mBAAmB;IACnB,2BAA2B;IAC3B,mBAAmB;IACnB,kBAAkB;IAClB,iBAAiB;EACnB;EAEC;MACG,kBAAkB;MAClB,SAAS;MACT,iBAAiB;IACnB;EAEF;IACE,qBAAqB;IACrB,WAAW;EACb;EAEA;IACE,2BAA2B;IAC3B,cAAc;EAChB;EAEA;IACE,aAAa;IACb,mBAAmB;IACnB,2BAA2B;IAC3B,mBAAmB;EACrB;EAEA;IACE,WAAW;IACX,YAAY;IACZ,kBAAkB;IAClB,aAAa;IACb,mBAAmB;IACnB,uBAAuB;IACvB,mBAAmB;IACnB,8BAA8B;IAC9B,cAAc;IACd,iBAAiB;IACjB,eAAe;EACjB;EAEA;IACE,iBAAiB;IACjB,gBAAgB;IAChB,kBAAkB;IAClB,mBAAmB;EACrB;EAEA;IACE,aAAa;IACb,mBAAmB;IACnB,eAAe;IACf,2BAA2B;IAC3B,mBAAmB;EACrB;EAEA;IACE,qCAAqC;IACrC,qBAAqB;IACrB,oBAAoB;IACpB,uBAAuB;IACvB,qBAAqB;IACrB,iBAAiB;EAUnB;EARE;;;OAGG;EAEH;MACE,cAAc;IAChB;EAEF;IACE,cAAc;EAChB;;AAGF;EACE,aAAa;EACb,eAAe;EACf,cAAc;EACd,iBAAiB;AACnB;;AAEA;EACE,eAAe;AAajB;;AAXE;IACE,UAAU;IACV,WAAW;IACX,YAAY;IACZ,kBAAkB;IAClB,iBAAiB;EACnB;;AAEA;IACE,gBAAgB;EAClB;;AAGF;EACE,cAAc;EACd,eAAe;EACf,iBAAiB;AACnB;;AAEA;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,iBAAiB;EACjB,eAAe;AACjB;;AAEA;EACE,cAAc;EACd,mBAAmB;AACrB;;AAEA;EACE,uBAAuB;EACvB,kBAAkB;AAKpB;;AAHE;IACE,iBAAiB;EACnB;;AAGF;EACE,YAAY;EACZ,iBAAiB;AACnB;;AAEA,YAAY;AACZ;EACE,eAAe;EACf,WAAW;AACb;;AAEA;EACE,qBAAqB;EACrB,gBAAgB;EAChB,YAAY;EACZ,WAAW;EACX,mBAAmB;EACnB,kBAAkB;EAClB,yBAAyB;EACzB,+BAA+B;EAC/B,WAAW;AACb;;AAEA;EACE,cAAc;EACd,eAAe;AACjB;;AAEA;EACE,kBAAkB;EAClB,gBAAgB;AAMlB;;AAJE;IACE,gBAAgB;IAChB,iBAAiB;EACnB;;AAGF;EACE,qBAAqB;EACrB,WAAW;EACX,aAAa;EACb,mBAAmB;AACrB",sourcesContent:[`.container {
  tr, td {
    padding: 0 !important;
    margin: 0 !important;
    height: 50px;
  }

  /* :global(.next-table-cell-wrapper) {
    font-size: 14px !important;
  } */

  tr > th > :global(.next-table-cell-wrapper) {
    padding: 0 0 0 16px !important;
    /* font-size: 14px !important;
    font-weight: bold !important; */
    height: 52px !important;
    line-height: 52px !important;
    background-color: #fafafa;
  }

  tr > td > :global(.next-table-cell-wrapper) {
    padding: 0 0 0 16px !important;
  }

  tr td:first-child:hover {
    cursor: pointer;
  }

  a {
    text-decoration: none;
  }

  title {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
    height: $rowHeight;
    line-height: 22px;
  }

   .listIcon {
      position: relative;
      bottom: 0;
      margin-right: 3px;
    }
  
  .title:hover {
    text-decoration: none;
    opacity: .8;
  }

  .displayTitle {
    /* display: inline-block; */
    color: #0070CC;
  }

  .apps {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
  }

  .app {
    width: 25px;
    height: 25px;
    border-radius: 50%;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    font-family: PingFangSC-Medium;
    color: #FFFFFF;
    margin-right: 5px;
    cursor: pointer;
  }

  .homePagination {
    text-align: right;
    margin-top: 16px;
    margin-right: 16px;
    margin-bottom: 30px;
  }

  .optGroup {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: flex-start;
    align-items: center;
  }
  
  .opt {
    /* font-family: PingFangSC-Regular; */
    /* font-size: 14px; */
    /* color: #0066CC; */
    /* letter-spacing: 0; */
    /* cursor: pointer; */
    margin-right: 8px;
  
    /* &.disable {
      color: #BFBFBF;
      cursor: not-allowed;
    } */
  
    &.warning {
      color: #FF4F4C;
    }
  }
  .opt:first-child {
    margin-left: 0;
  }
}

.cloneTitle {
  opacity: 0.65;
  font-size: 14px;
  color: #000000;
  letter-spacing: 0;
}

.cloneBtnRow {
  margin-top: 8px;

  :global(.next-btn) {
    padding: 0;
    width: 48px;
    height: 24px;
    text-align: center;
    line-height: 24px;
  }

  :global(.next-btn-normal) {
    margin-left: 8px;
  }
}

.helpIcon {
  color: #D93026;
  cursor: pointer;
  margin-right: 4px;
}

.optYellow {
  font-family: PingFangSC-Regular;
  font-size: 14px;
  color: #0066CC;
  letter-spacing: 0;
  cursor: pointer;
}

.disable {
  color: #BFBFBF;
  cursor: not-allowed;
}

.ulList {
  list-style-type: square;
  padding-left: 10px;
  
  .baoollnList {
    line-height: 32px;
  }
}

.deleteTips {
  width: 400px;
  line-height: 24px;
}

/* \u6F14\u7EC3\u8D44\u6E90\u5305\u90E8\u5206 */
.paidTips {
  font-size: 12px;
  color: #333;
}

.packName {
  display: inline-block;
  padding: 2px 2px;
  height: 16px;
  width: 44px;
  background: #0091FF;
  border-radius: 2px;
  background-color: #0091FF;
  font-family: PingFangSC-Regular;
  color: #fff;
}

.action {
  color: #0070CC;
  cursor: pointer;
}

.tipsContent {
  padding-left: 20px;
  list-style: disc;

  .startTipsList {
    min-height: 22px;
    line-height: 22px;
  }
}

.warnContent {
  display: inline-block;
  width: 100%;
  padding: 15px;
  background: #FFF7DB;
}`],sourceRoot:""}]),i.locals={container:"index__container__5pnJG",listIcon:"index__listIcon__5VGEE",title:"index__title__utO2v",displayTitle:"index__displayTitle__LuH1p",apps:"index__apps__Km2AZ",app:"index__app__QY2yH",homePagination:"index__homePagination__+jylf",optGroup:"index__optGroup__PlYxl",opt:"index__opt__XATIF",warning:"index__warning__OIkiA",cloneTitle:"index__cloneTitle__1xIZC",cloneBtnRow:"index__cloneBtnRow__8k53-",helpIcon:"index__helpIcon__KbxL0",optYellow:"index__optYellow__ZlCap",disable:"index__disable__FXeNR",ulList:"index__ulList__ebeQV",baoollnList:"index__baoollnList__Z4HhB",deleteTips:"index__deleteTips__wj5Ms",paidTips:"index__paidTips__jUmR3",packName:"index__packName__aWYnq",action:"index__action__bBvr7",tipsContent:"index__tipsContent__OPmgX",startTipsList:"index__startTipsList__IZGyD",warnContent:"index__warnContent__I1koO"};const n=i},49162:(k,f,e)=>{"use strict";e.d(f,{Z:()=>n});var r=e(60994),h=e.n(r),d=e(93476),p=e.n(d),i=p()(h());i.push([k.id,`.index__DialogExperience__F7tpR {
  width: 80%;
}
  .index__DialogExperience__F7tpR .next-dialog-body {
    height: 600px;
    overflow-y: scroll;
    padding-top: 0px;
    width: 100%;
  }

.index__warp__QFAzX {
  width: 100%;
  padding: 0px 0px 16px 16px;
}

.index__warp__QFAzX .index__search__v-tVI {
    width: 33.8%;
  }

.index__warp__QFAzX .index__TemplatesContent__v05S\\+ {
    width: 100%;
    margin-top: 16px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap; 
  }

.index__warp__QFAzX .index__TemplatesContent__v05S\\+ .index__cardList__FSpmZ {
      width: 23.5%;
      height: 26.9%; 
      margin-right: 16px;
      margin-bottom: 16px;

    }

.index__warp__QFAzX .index__TemplatesContent__v05S\\+ .index__cardList__FSpmZ:nth-child(4n) {
        margin-right: 0px !important;
      }

.index__warp__QFAzX .index__Pagination__2lgT6 {
    margin-top: 20px;
    float: right;
  }

.index__warp__QFAzX .index__textCenter__P2ZYK {
    margin-left: 45%;
    margin-top: 20%;
  }

.index__warp__QFAzX .index__emptyCard__JbFcB {
    padding: 16px;
    border: 1px solid #dedede;
    cursor: pointer;
    width: 23.5%;
    /* height: 262px; */
    margin-right: 16px;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    justify-content: center;

  }

.index__warp__QFAzX .index__emptyCard__JbFcB:hover {
      border: 1px solid #0070CC;
      box-shadow: 0 0 4px 0 #0070CC;
      border-radius: 3px;
    }

.index__warp__QFAzX .index__emptyCard__JbFcB .index__iconContent__NxmAv {
      text-align: center;
      color: #888;
    }

.index__warp__QFAzX .index__emptyCard__JbFcB .index__addIcon__bksA0 {
      font-size: 36px;
      margin-bottom: 5px;
    }

.index__warp__QFAzX .index__emptyCard__JbFcB .index__addIcon__bksA0::before {
        width: 36px !important;
        height: 36px !important;
        font-size: 36px !important;
      }`,"",{version:3,sources:["webpack://./pages/Chaos/ExperimentList/ExpertiseCard/index.css"],names:[],mappings:"AAAA;EACE,UAAU;AAOZ;EANE;IACE,aAAa;IACb,kBAAkB;IAClB,gBAAgB;IAChB,WAAW;EACb;;AAGF;EACE,WAAW;EACX,0BAA0B;AAsE5B;;AApEE;IACE,YAAY;EACd;;AAEA;IACE,WAAW;IACX,gBAAgB;IAChB,aAAa;IACb,2BAA2B;IAC3B,eAAe;EAajB;;AAXE;MACE,YAAY;MACZ,aAAa;MACb,kBAAkB;MAClB,mBAAmB;;IAMrB;;AAJE;QACE,4BAA4B;MAC9B;;AAKJ;IACE,gBAAgB;IAChB,YAAY;EACd;;AAEA;IACE,gBAAgB;IAChB,eAAe;EACjB;;AAEA;IACE,aAAa;IACb,yBAAyB;IACzB,eAAe;IACf,YAAY;IACZ,mBAAmB;IACnB,kBAAkB;IAClB,mBAAmB;IACnB,aAAa;IACb,mBAAmB;IACnB,uBAAuB;;EAuBzB;;AArBE;MACE,yBAAyB;MACzB,6BAA6B;MAC7B,kBAAkB;IACpB;;AAEA;MACE,kBAAkB;MAClB,WAAW;IACb;;AAEA;MACE,eAAe;MACf,kBAAkB;IAMpB;;AALE;QACE,sBAAsB;QACtB,uBAAuB;QACvB,0BAA0B;MAC5B",sourcesContent:[`.DialogExperience {
  width: 80%;
  :global(.next-dialog-body) {
    height: 600px;
    overflow-y: scroll;
    padding-top: 0px;
    width: 100%;
  }
}

.warp {
  width: 100%;
  padding: 0px 0px 16px 16px;

  .search {
    width: 33.8%;
  }

  .TemplatesContent {
    width: 100%;
    margin-top: 16px;
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;

    .cardList {
      width: 23.5%;
      height: 26.9%; 
      margin-right: 16px;
      margin-bottom: 16px;

      &:nth-child(4n) {
        margin-right: 0px !important;
      }

    } 
  }

  .Pagination {
    margin-top: 20px;
    float: right;
  }

  .textCenter {
    margin-left: 45%;
    margin-top: 20%;
  }

  .emptyCard {
    padding: 16px;
    border: 1px solid #dedede;
    cursor: pointer;
    width: 23.5%;
    /* height: 262px; */
    margin-right: 16px;
    margin-bottom: 16px;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      border: 1px solid #0070CC;
      box-shadow: 0 0 4px 0 #0070CC;
      border-radius: 3px;
    }

    .iconContent {
      text-align: center;
      color: #888;
    }

    .addIcon {
      font-size: 36px;
      margin-bottom: 5px;
      &::before {
        width: 36px !important;
        height: 36px !important;
        font-size: 36px !important;
      }
    }

  }
}`],sourceRoot:""}]),i.locals={DialogExperience:"index__DialogExperience__F7tpR",warp:"index__warp__QFAzX",search:"index__search__v-tVI",TemplatesContent:"index__TemplatesContent__v05S+",cardList:"index__cardList__FSpmZ",Pagination:"index__Pagination__2lgT6",textCenter:"index__textCenter__P2ZYK",emptyCard:"index__emptyCard__JbFcB",iconContent:"index__iconContent__NxmAv",addIcon:"index__addIcon__bksA0"};const n=i},82457:(k,f,e)=>{"use strict";e.d(f,{Z:()=>n});var r=e(60994),h=e.n(r),d=e(93476),p=e.n(d),i=p()(h());i.push([k.id,`.index__wrapper__The9Y {
  width: 100%;
  display: flex;
  background: #fff;
}

.index__icon__Dyzo\\+ {
  margin-right: 8px;
  position: relative;
  bottom: 1px;
}

.index__statisticDone__CPORB,
.index__statisticUnDone__0FPod,
.index__statisticTotal__KkmUb {
  height: 80px;
  border: 1px solid #ebebeb;
}

/* .doneBox {
  width: 240px;
} */

.index__statisticTotal__KkmUb,
.index__doneBox__9mMUs {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.index__statisticTotal__KkmUb {
  flex-grow: 1;
  margin-left: 20px;
}

.index__statisticUnDone__0FPod {
  width: 22%;
  display: flex;
  justify-content: space-between;
}

.index__statisticDone__CPORB {
  width: 60%;
  display: flex;
  /* flex-direction: row; */
  margin-right: 20px;
}

.index__statisticDone__CPORB .index__doneBox__9mMUs {
    width: 30%;
  }

.index__statisticDone__CPORB .index__separator__6PDZS {
    margin-top: 20px;
    width: 1px;
    height: 40px;
    background: #d8d8d8;
  }

.index__statisticDone__CPORB .index__detailBox__wOtJm {
    position: relative;
    flex-grow: 1;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

.index__statisticDone__CPORB .index__topTip__adgqN {
    position: absolute;
    top: 0;
    width: 100%;
    height: 18px;
    text-align: center;
  }

.index__statisticDone__CPORB .index__failedBox__HiUdy,
  .index__statisticDone__CPORB .index__runningBox__m4W6g,
  .index__statisticDone__CPORB .index__successBox__Ryvd9 {
    padding-left: 26px;
    padding-top: 30px;
    font-family: PingFangSC-Regular;
    font-size: 16px;
    color: #333333;
    letter-spacing: -0.23px;
  }

.index__statisticDone__CPORB .index__successBox__Ryvd9 {
    padding-right: 26px;
  }

.index__statisticDone__CPORB .index__detailFont__0DeNJ {
    font-weight: 700;
  }

.index__title__AnCHZ {
  font-family: PingFangSC-Regular;
  font-size: 16px;
  color: #333333;
  position: relative;
  margin: auto 0;
  padding-left: 16px;
}

.index__title__AnCHZ:before {
  position: relative;
  top: -1px;
  margin-right: 5px;
  content: "";
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 4px;
}

.index__statisticDone__CPORB .index__title__AnCHZ:before {
  background: #0070cc;
}

.index__statisticUnDone__0FPod .index__title__AnCHZ:before {
  background: #aaa;
}

.index__statisticTotal__KkmUb .index__title__AnCHZ:before {
  background: #a5c3de;
}

.index__number__\\+s3d3 {
  margin-right: 24px;
  font-family: PingFangSC-Semibold;
  font-size: 28px;
  color: #333;
  letter-spacing: 0;
  text-align: right;
  line-height: 80px;
}
`,"",{version:3,sources:["webpack://./pages/Chaos/ExperimentList/Statistic/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,aAAa;EACb,gBAAgB;AAClB;;AAEA;EACE,iBAAiB;EACjB,kBAAkB;EAClB,WAAW;AACb;;AAEA;;;EAGE,YAAY;EACZ,yBAAyB;AAC3B;;AAEA;;GAEG;;AAEH;;EAEE,aAAa;EACb,mBAAmB;EACnB,8BAA8B;AAChC;;AAEA;EACE,YAAY;EACZ,iBAAiB;AACnB;;AAEA;EACE,UAAU;EACV,aAAa;EACb,8BAA8B;AAChC;;AAEA;EACE,UAAU;EACV,aAAa;EACb,yBAAyB;EACzB,kBAAkB;AA+CpB;;AA7CE;IACE,UAAU;EACZ;;AAEA;IACE,gBAAgB;IAChB,UAAU;IACV,YAAY;IACZ,mBAAmB;EACrB;;AAEA;IACE,kBAAkB;IAClB,YAAY;IACZ,aAAa;IACb,mBAAmB;IACnB,8BAA8B;EAChC;;AAEA;IACE,kBAAkB;IAClB,MAAM;IACN,WAAW;IACX,YAAY;IACZ,kBAAkB;EACpB;;AAEA;;;IAGE,kBAAkB;IAClB,iBAAiB;IACjB,+BAA+B;IAC/B,eAAe;IACf,cAAc;IACd,uBAAuB;EACzB;;AAEA;IACE,mBAAmB;EACrB;;AAEA;IACE,gBAAgB;EAClB;;AAGF;EACE,+BAA+B;EAC/B,eAAe;EACf,cAAc;EACd,kBAAkB;EAClB,cAAc;EACd,kBAAkB;AACpB;;AAEA;EACE,kBAAkB;EAClB,SAAS;EACT,iBAAiB;EACjB,WAAW;EACX,qBAAqB;EACrB,UAAU;EACV,WAAW;EACX,kBAAkB;AACpB;;AAEA;EACE,mBAAmB;AACrB;;AAEA;EACE,gBAAgB;AAClB;;AAEA;EACE,mBAAmB;AACrB;;AAEA;EACE,kBAAkB;EAClB,gCAAgC;EAChC,eAAe;EACf,WAAW;EACX,iBAAiB;EACjB,iBAAiB;EACjB,iBAAiB;AACnB",sourcesContent:[`.wrapper {
  width: 100%;
  display: flex;
  background: #fff;
}

.icon {
  margin-right: 8px;
  position: relative;
  bottom: 1px;
}

.statisticDone,
.statisticUnDone,
.statisticTotal {
  height: 80px;
  border: 1px solid #ebebeb;
}

/* .doneBox {
  width: 240px;
} */

.statisticTotal,
.doneBox {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.statisticTotal {
  flex-grow: 1;
  margin-left: 20px;
}

.statisticUnDone {
  width: 22%;
  display: flex;
  justify-content: space-between;
}

.statisticDone {
  width: 60%;
  display: flex;
  /* flex-direction: row; */
  margin-right: 20px;

  .doneBox {
    width: 30%;
  }

  .separator {
    margin-top: 20px;
    width: 1px;
    height: 40px;
    background: #d8d8d8;
  }

  .detailBox {
    position: relative;
    flex-grow: 1;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .topTip {
    position: absolute;
    top: 0;
    width: 100%;
    height: 18px;
    text-align: center;
  }

  .failedBox,
  .runningBox,
  .successBox {
    padding-left: 26px;
    padding-top: 30px;
    font-family: PingFangSC-Regular;
    font-size: 16px;
    color: #333333;
    letter-spacing: -0.23px;
  }

  .successBox {
    padding-right: 26px;
  }

  .detailFont {
    font-weight: 700;
  }
}

.title {
  font-family: PingFangSC-Regular;
  font-size: 16px;
  color: #333333;
  position: relative;
  margin: auto 0;
  padding-left: 16px;
}

.title:before {
  position: relative;
  top: -1px;
  margin-right: 5px;
  content: "";
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 4px;
}

.statisticDone .title:before {
  background: #0070cc;
}

.statisticUnDone .title:before {
  background: #aaa;
}

.statisticTotal .title:before {
  background: #a5c3de;
}

.number {
  margin-right: 24px;
  font-family: PingFangSC-Semibold;
  font-size: 28px;
  color: #333;
  letter-spacing: 0;
  text-align: right;
  line-height: 80px;
}
`],sourceRoot:""}]),i.locals={wrapper:"index__wrapper__The9Y",icon:"index__icon__Dyzo+",statisticDone:"index__statisticDone__CPORB",statisticUnDone:"index__statisticUnDone__0FPod",statisticTotal:"index__statisticTotal__KkmUb",doneBox:"index__doneBox__9mMUs",separator:"index__separator__6PDZS",detailBox:"index__detailBox__wOtJm",topTip:"index__topTip__adgqN",failedBox:"index__failedBox__HiUdy",runningBox:"index__runningBox__m4W6g",successBox:"index__successBox__Ryvd9",detailFont:"index__detailFont__0DeNJ",title:"index__title__AnCHZ",number:"index__number__+s3d3"};const n=i},36398:(k,f,e)=>{"use strict";e.d(f,{Z:()=>n});var r=e(60994),h=e.n(r),d=e(93476),p=e.n(d),i=p()(h());i.push([k.id,`.index__dingtalkIcon__Wkb3L {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.index__operations__mSWGw {
  padding: 24px 0px 14px 0px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}

.index__operations__mSWGw .index__stopButton__GIXli {
    width: 88px;
    background-color: #d93026;
    margin-left: 4px;
    color: #ffffff;
  }

.index__operations__mSWGw .index__createButton__KR9AA,
  .index__operations__mSWGw .index__stopButton__GIXli {
    /* font-family: PingFangSC-Regular; */
    letter-spacing: -0.2px;
    line-height: 32px;
  }

.index__operations__mSWGw .index__search__gexSl {
    width: 260px;
    margin-left: 4px;
  }

.index__operations__mSWGw .index__select__56xJp {
    width: 240px;
    margin-left: 4px;
    margin-right: 6px;
  }

.index__operations__mSWGw .index__schedulerCb__1Fuym {
    font-weight: 400;
    height: 32px;
    line-height: 32px;
    cursor: pointer;
    
    margin-left: 8px;
  }

.index__operations__mSWGw .index__schedulerCb__1Fuym > span {
      color: #333!important;
    }

.index__message__kxpNb {
  background: #fff;
  padding: 4px 8px;
  box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.13);
  border-style: solid;
  border-color: #ffffff;
  border-radius: 2px;
}

.index__switch__tPTAO {
  line-height: 32px;
  margin-left: 8px;
}

.index__switch__tPTAO .next-switch {
    position: relative;
    top: 3px;
    left: 2px;
  }

.index__getHref__6HW46 {
  color: '#0070cc';
}

.index__getHref__6HW46:hover {
    text-decoration: underline;
    cursor: pointer;
  }
/* .createButton {
  &:hover > i {
    transform: rotate(90deg);
  }
} */
.index__createMenu__IQjeU > li:hover {
    background: #bee2ff!important;
  }
.index__createMenu__IQjeU > li:last-child {
    border-top: 1px solid #ddd;
    margin: 4px 0;
  }`,"",{version:3,sources:["webpack://./pages/Chaos/ExperimentList/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,YAAY;EACZ,iBAAiB;AACnB;;AAEA;EACE,0BAA0B;EAC1B,aAAa;EACb,mBAAmB;EACnB,8BAA8B;EAC9B,mBAAmB;AAoCrB;;AAlCE;IACE,WAAW;IACX,yBAAyB;IACzB,gBAAgB;IAChB,cAAc;EAChB;;AAEA;;IAEE,qCAAqC;IACrC,sBAAsB;IACtB,iBAAiB;EACnB;;AACA;IACE,YAAY;IACZ,gBAAgB;EAClB;;AAEA;IACE,YAAY;IACZ,gBAAgB;IAChB,iBAAiB;EACnB;;AACA;IACE,gBAAgB;IAChB,YAAY;IACZ,iBAAiB;IACjB,eAAe;;IAEf,gBAAgB;EAIlB;;AAHE;MACE,qBAAqB;IACvB;;AAIJ;EACE,gBAAgB;EAChB,gBAAgB;EAChB,+CAA+C;EAC/C,mBAAmB;EACnB,qBAAqB;EACrB,kBAAkB;AACpB;;AAEA;EACE,iBAAiB;EACjB,gBAAgB;AAMlB;;AALE;IACE,kBAAkB;IAClB,QAAQ;IACR,SAAS;EACX;;AAGF;EACE,gBAAgB;AAMlB;;AAJE;IACE,0BAA0B;IAC1B,eAAe;EACjB;AAEF;;;;GAIG;AAED;IACE,6BAA6B;EAC/B;AACA;IACE,0BAA0B;IAC1B,aAAa;EACf",sourcesContent:[`.dingtalkIcon {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}

.operations {
  padding: 24px 0px 14px 0px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;

  .stopButton {
    width: 88px;
    background-color: #d93026;
    margin-left: 4px;
    color: #ffffff;
  }

  .createButton,
  .stopButton {
    /* font-family: PingFangSC-Regular; */
    letter-spacing: -0.2px;
    line-height: 32px;
  }
  .search {
    width: 260px;
    margin-left: 4px;
  }

  .select {
    width: 240px;
    margin-left: 4px;
    margin-right: 6px;
  }
  .schedulerCb {
    font-weight: 400;
    height: 32px;
    line-height: 32px;
    cursor: pointer;
    
    margin-left: 8px;
    > span {
      color: #333!important;
    }
  }
}

.message {
  background: #fff;
  padding: 4px 8px;
  box-shadow: 0px 2px 8px 0px rgba(0, 0, 0, 0.13);
  border-style: solid;
  border-color: #ffffff;
  border-radius: 2px;
}

.switch {
  line-height: 32px;
  margin-left: 8px;
  :global(.next-switch) {
    position: relative;
    top: 3px;
    left: 2px;
  }
}

.getHref {
  color: '#0070cc';

  &:hover {
    text-decoration: underline;
    cursor: pointer;
  }
}
/* .createButton {
  &:hover > i {
    transform: rotate(90deg);
  }
} */
.createMenu {
  > li:hover {
    background: #bee2ff!important;
  }
  > li:last-child {
    border-top: 1px solid #ddd;
    margin: 4px 0;
  }
}`],sourceRoot:""}]),i.locals={dingtalkIcon:"index__dingtalkIcon__Wkb3L",operations:"index__operations__mSWGw",stopButton:"index__stopButton__GIXli",createButton:"index__createButton__KR9AA",search:"index__search__gexSl",select:"index__select__56xJp",schedulerCb:"index__schedulerCb__1Fuym",message:"index__message__kxpNb",switch:"index__switch__tPTAO",getHref:"index__getHref__6HW46",createMenu:"index__createMenu__IQjeU"};const n=i},29522:(k,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>n});var r=e(1892),h=e.n(r),d=e(93705),p={};p.insert="head",p.singleton=!1;var i=h()(d.Z,p);const n=d.Z.locals||{}},53256:(k,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>n});var r=e(1892),h=e.n(r),d=e(46812),p={};p.insert="head",p.singleton=!1;var i=h()(d.Z,p);const n=d.Z.locals||{}},88972:(k,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>n});var r=e(1892),h=e.n(r),d=e(49162),p={};p.insert="head",p.singleton=!1;var i=h()(d.Z,p);const n=d.Z.locals||{}},65328:(k,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>n});var r=e(1892),h=e.n(r),d=e(82457),p={};p.insert="head",p.singleton=!1;var i=h()(d.Z,p);const n=d.Z.locals||{}},18390:(k,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>n});var r=e(1892),h=e.n(r),d=e(36398),p={};p.insert="head",p.singleton=!1;var i=h()(d.Z,p);const n=d.Z.locals||{}}}]);

//# sourceMappingURL=344.bundle.js.map