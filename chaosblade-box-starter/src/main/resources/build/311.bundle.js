(self.webpackChunk=self.webpackChunk||[]).push([[311],{38856:function(x,f,e){"use strict";var u=this&&this.__assign||function(){return u=Object.assign||function(_){for(var n,i=1,l=arguments.length;i<l;i++){n=arguments[i];for(var d in n)Object.prototype.hasOwnProperty.call(n,d)&&(_[d]=n[d])}return _},u.apply(this,arguments)},y=this&&this.__importDefault||function(_){return _&&_.__esModule?_:{default:_}};Object.defineProperty(f,"__esModule",{value:!0});var o=y(e(27378)),A=e(30156),s=y(e(55839)),c=y(e(43387)),I=e(67056),C=function(_){return o.default.createElement(A.Card,u({showTitleBullet:!1},_))};s.default(C,A.Card),C.DropDownActions=function(_){var n=_.actions,i=I.useCssVar("--alicloudfe-components-theme").trim(),l=function(){return i.startsWith("hybridcloud")?o.default.createElement(A.Icon,{className:"hybridcloud-card-dropdown-actions-icon",type:"ellipsis",size:"large"}):i.startsWith("yunxiao")?o.default.createElement(A.Icon,{className:"yunxiao-card-dropdown-actions-icon",type:"ellipsis"}):o.default.createElement(A.Icon,{type:"ellipsis-vertical",size:"small"})}();return o.default.createElement(A.Dropdown,{trigger:l,triggerType:"click"},o.default.createElement(A.Menu,null,n.map(function(d,v){var a=d.label,h=d.onClick;return o.default.createElement(A.Menu.Item,{onClick:h,key:v},a)})))};var g=function(_){var n=_.collapsed,i=_.onCollapsedChange,l=_.prefix,d=_.children,v=d===void 0?"\u5C55\u5F00\u66F4\u591A":d;return o.default.createElement("div",{className:l+"card-collapsable-tail",onClick:function(){return i(!n)}},v,n?o.default.createElement(A.Icon,{type:"3212"}):o.default.createElement(A.Icon,{type:"3213"}))};C.CollapsableTail=c.default.config(g);var r=function(_){var n=_.collapsed,i=_.onCollapsedChange,l=_.prefix,d=_.children,v=d===void 0?"\u6807\u9898":d;return o.default.createElement("div",{className:l+"card-collapsable-head",onClick:function(){return i(!n)}},v,n?o.default.createElement(A.Icon,{type:"3212"}):o.default.createElement(A.Icon,{type:"3213"}))};C.CollapsableHead=c.default.config(r),f.default=C},91714:function(x,f,e){"use strict";var u=this&&this.__assign||function(){return u=Object.assign||function(C){for(var g,r=1,_=arguments.length;r<_;r++){g=arguments[r];for(var n in g)Object.prototype.hasOwnProperty.call(g,n)&&(C[n]=g[n])}return C},u.apply(this,arguments)},y=this&&this.__importDefault||function(C){return C&&C.__esModule?C:{default:C}};Object.defineProperty(f,"__esModule",{value:!0});var o=y(e(27378)),A=e(30156),s=y(e(55839)),c=e(46949),I=function(C){return o.default.createElement(A.Step,u({stretch:!0},C))};s.default(I,A.Step),f.default=c.withThemeClass(I)},16664:function(x,f,e){"use strict";var u=this&&this.__assign||function(){return u=Object.assign||function(r){for(var _,n=1,i=arguments.length;n<i;n++){_=arguments[n];for(var l in _)Object.prototype.hasOwnProperty.call(_,l)&&(r[l]=_[l])}return r},u.apply(this,arguments)},y=this&&this.__rest||function(r,_){var n={};for(var i in r)Object.prototype.hasOwnProperty.call(r,i)&&_.indexOf(i)<0&&(n[i]=r[i]);if(r!=null&&typeof Object.getOwnPropertySymbols=="function")for(var l=0,i=Object.getOwnPropertySymbols(r);l<i.length;l++)_.indexOf(i[l])<0&&Object.prototype.propertyIsEnumerable.call(r,i[l])&&(n[i[l]]=r[i[l]]);return n},o=this&&this.__importDefault||function(r){return r&&r.__esModule?r:{default:r}};Object.defineProperty(f,"__esModule",{value:!0});var A=o(e(27378)),s=o(e(23615)),c=o(e(60042)),I=e(30156),C=e(66693),g=function(r){var _=r.type,n=r.className,i=y(r,["type","className"]);return A.default.createElement(I.Tag,u({},i,{className:c.default(C.COLORED_CLASS_NAME,C.COLORED_CLASS_NAME+"-"+_,n)}))};g.propTypes=u(u({},I.Tag.propTypes),{type:s.default.oneOf(Object.values(C.Color)),className:s.default.string}),g.defaultProps={type:C.Color.LIGHT_STEEL_BLUE},g[C.PROTECTED_TYPE]="Tag",f.default=g},79148:function(x,f,e){"use strict";var u=this&&this.__assign||function(){return u=Object.assign||function(d){for(var v,a=1,h=arguments.length;a<h;a++){v=arguments[a];for(var t in v)Object.prototype.hasOwnProperty.call(v,t)&&(d[t]=v[t])}return d},u.apply(this,arguments)},y=this&&this.__createBinding||(Object.create?function(d,v,a,h){h===void 0&&(h=a),Object.defineProperty(d,h,{enumerable:!0,get:function(){return v[a]}})}:function(d,v,a,h){h===void 0&&(h=a),d[h]=v[a]}),o=this&&this.__setModuleDefault||(Object.create?function(d,v){Object.defineProperty(d,"default",{enumerable:!0,value:v})}:function(d,v){d.default=v}),A=this&&this.__importStar||function(d){if(d&&d.__esModule)return d;var v={};if(d!=null)for(var a in d)a!=="default"&&Object.hasOwnProperty.call(d,a)&&y(v,d,a);return o(v,d),v},s=this&&this.__importDefault||function(d){return d&&d.__esModule?d:{default:d}};Object.defineProperty(f,"__esModule",{value:!0});var c=A(e(27378)),I=s(e(23615)),C=s(e(60042)),g=e(30156),r=s(e(16664)),_=e(66693),n=g.Tag.Group,i=[_.Color.LIGHT_STEEL_BLUE,_.Color.PLUM,_.Color.MISTY_ROSE,_.Color.LIGHT_GOLDENROD_YELLOW,_.Color.PALE_GREEN],l=function(d){var v=d.className,a=d.style,h=d.avaliableColors,t=h===void 0?i:h,p=d.children;return c.default.createElement(n,{className:C.default(_.COLORED_GROUP_CLASS_NAME,v),style:a},c.Children.map(p,function(m,P){var E=m;try{var R=m.type[_.PROTECTED_TYPE];R==="Tag"&&(E=c.default.createElement(r.default,u({},m.props,{type:t[P%5]})))}catch(S){}return E}))};l.propTypes={className:I.default.string,style:I.default.objectOf(I.default.any),avaliableColors:I.default.arrayOf(I.default.string),children:I.default.node},f.default=l},66693:(x,f)=>{"use strict";Object.defineProperty(f,"__esModule",{value:!0}),f.PROTECTED_TYPE=f.COLORED_GROUP_CLASS_NAME=f.COLORED_CLASS_NAME=f.Color=void 0,f.Color={LIGHT_STEEL_BLUE:"light-steel-blue",PLUM:"plum",MISTY_ROSE:"misty-rose",LIGHT_GOLDENROD_YELLOW:"light-goldenrod-yellow",PALE_GREEN:"pale-green",SILVER:"silver",GRAY:"gray"},f.COLORED_CLASS_NAME="wind-tag-colored",f.COLORED_GROUP_CLASS_NAME=f.COLORED_CLASS_NAME+"-group",f.PROTECTED_TYPE="__WIND_TAG_"},51834:function(x,f,e){"use strict";var u=this&&this.__importDefault||function(c){return c&&c.__esModule?c:{default:c}};Object.defineProperty(f,"__esModule",{value:!0}),f.wrap=void 0;var y=e(66693),o=u(e(16664)),A=u(e(79148));function s(c){return c.Colored=o.default,c.ColoredGroup=A.default,c[y.PROTECTED_TYPE]="Tag",c}f.wrap=s},36939:function(x,f,e){"use strict";var u=this&&this.__assign||function(){return u=Object.assign||function(n){for(var i,l=1,d=arguments.length;l<d;l++){i=arguments[l];for(var v in i)Object.prototype.hasOwnProperty.call(i,v)&&(n[v]=i[v])}return n},u.apply(this,arguments)},y=this&&this.__rest||function(n,i){var l={};for(var d in n)Object.prototype.hasOwnProperty.call(n,d)&&i.indexOf(d)<0&&(l[d]=n[d]);if(n!=null&&typeof Object.getOwnPropertySymbols=="function")for(var v=0,d=Object.getOwnPropertySymbols(n);v<d.length;v++)i.indexOf(d[v])<0&&Object.prototype.propertyIsEnumerable.call(n,d[v])&&(l[d[v]]=n[d[v]]);return l},o=this&&this.__importDefault||function(n){return n&&n.__esModule?n:{default:n}};Object.defineProperty(f,"__esModule",{value:!0});var A=e(30156),s=o(e(27378)),c=e(46949),I=o(e(55839)),C=e(51834),g=e(67056),r=o(e(60042)),_=C.wrap(c.withThemeClass(s.default.forwardRef(function(n,i){var l,d=n.children,v=n.color,a=n.prefix,h=a===void 0?"next-":a,t=n.className,p=y(n,["className"]),m=g.useCssVar("--alicloudfe-components-theme").trim();return m==="hybridcloud"||m==="hybridcloud-dark"||m==="yunxiao"||m==="yunxiao-dark"?s.default.createElement(A.Tag,u({ref:i,className:r.default((l={},l[h+"tag-custom-"+v]=!0,l),t)},p),d):s.default.createElement(A.Tag,u({},n,{ref:i}),d)})));I.default(_,A.Tag),_.displayName=A.Tag.displayName,f.default=_},64780:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(72153),e(27378),e(66697)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_){"use strict";var n=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=n(g),r=l(r),_=n(_);function i(a){if(typeof c!="function")return null;var h=new c,t=new c;return(i=function(m){return m?t:h})(a)}function l(a,h){if(!h&&a&&a.__esModule)return a;if(a===null||A(a)!=="object"&&typeof a!="function")return{default:a};var t=i(h);if(t&&t.has(a))return t.get(a);var p={},m=s&&I;for(var P in a)if(P!=="default"&&Object.prototype.hasOwnProperty.call(a,P)){var E=m?I(a,P):null;E&&(E.get||E.set)?s(p,P,E):p[P]=a[P]}return p.default=a,t&&t.set(a,p),p}var d=function(h){var t=h.step,p=h.handleComplete,m=h.handlePrevOrNextStep;return r.default.createElement("div",{style:{display:"flex",justifyContent:"flex-end",marginTop:25}},t>0&&t!==3&&r.default.createElement(g.default,{type:"primary",onClick:function(){return m(!1)}},r.default.createElement(_.default,null,"Pervious")),t>0&&t!==2&&r.default.createElement(g.default,{style:{marginLeft:"10px"},onClick:p},r.default.createElement(_.default,null,"cancel")))},v=(0,r.memo)(d);C.default=v})},91769:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(72153),e(36939),e(17225),e(94188),e(27378),e(66697),e(74248),e(5282)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n,i,l,d,v){"use strict";var a=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=a(g),r=a(r),_=a(_),n=a(n),i=t(i),l=a(l),d=a(d);function h(P){if(typeof c!="function")return null;var E=new c,R=new c;return(h=function(D){return D?R:E})(P)}function t(P,E){if(!E&&P&&P.__esModule)return P;if(P===null||A(P)!=="object"&&typeof P!="function")return{default:P};var R=h(E);if(R&&R.has(P))return R.get(P);var S={},D=s&&I;for(var O in P)if(O!=="default"&&Object.prototype.hasOwnProperty.call(P,O)){var W=D?I(P,O):null;W&&(W.get||W.set)?s(S,O,W):S[O]=P[O]}return S.default=P,R&&R.set(P,S),S}var p=function(E){var R=E.filterText,S=E.searchKeywordList,D=E.searchFilterKey,O=E.handleSearchChange,W=E.handleFilterSearch,F=E.handleFilterSearchChange,U=E.tagSearchClose,T=E.clearFilter;return i.default.createElement("div",{className:d.default.content},i.default.createElement(n.default,{value:R,filter:v.AGENT_SEARCH,defaultFilterValue:"PrivateIpList",onChange:O,onSearch:W,onFilterChange:F}),i.default.createElement("span",{className:d.default.info},i.default.createElement(l.default,null,"Currently only exact queries are supported")),i.default.createElement("div",null,S.length>0&&i.default.createElement("span",null,i.default.createElement(_.default,{type:"filter",size:"xs"}),"\xA0",i.default.createElement(l.default,null,"filter"),":"),S.length>0&&S.map(function(M){var B;return i.default.createElement(r.default.Closeable,{key:M,onClose:U,"data-id":M},((B=v.AGENT_SEARCH.find(function(N){return N.value===D}))===null||B===void 0?void 0:B.value)+": "+M)}),S.length>0&&i.default.createElement(g.default,{onClick:T,style:{background:"#fff",border:"none"}},i.default.createElement(l.default,null,"Empty"))))},m=(0,i.memo)(p);C.default=m})},31562:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587),C=e(83452),g=e(95315),r=e(63774),_=e(92937);(function(n,i){if(!0)!(y=[f,e(32009),e(12955),e(28757),e(57379),e(15286),e(93080),e(77809),e(88162),e(81853),e(30553),e(8583),e(27378),e(66697),e(98784),e(14798),e(68055),e(50064),e(10957),e(99328),e(14870),e(19e3)],u=i,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(n,i,l,d,v,a,h,t,p,m,P,E,R,S,D,O,W,F,U,T,M,B){"use strict";var N=e(67971);s(n,"__esModule",{value:!0}),n.default=void 0,i=N(i),l=N(l),d=N(d),v=N(v),a=N(a),h=N(h),t=N(t),p=N(p),m=N(m),P=N(P),E=N(E),R=J(R),S=N(S),D=N(D),O=N(O),W=N(W),F=N(F),B=N(B);function X(L){if(typeof c!="function")return null;var G=new c,z=new c;return(X=function(Q){return Q?z:G})(L)}function J(L,G){if(!G&&L&&L.__esModule)return L;if(L===null||A(L)!=="object"&&typeof L!="function")return{default:L};var z=X(G);if(z&&z.has(L))return z.get(L);var K={},Q=s&&I;for(var j in L)if(j!=="default"&&Object.prototype.hasOwnProperty.call(L,j)){var w=Q?I(L,j):null;w&&(w.get||w.set)?s(K,j,w):K[j]=L[j]}return K.default=L,z&&z.set(L,K),K}function ae(L,G){var z=C(L);if(g){var K=g(L);G&&(K=K.filter(function(Q){return I(L,Q).enumerable})),z.push.apply(z,K)}return z}function le(L){for(var G=1;G<arguments.length;G++){var z=arguments[G]!=null?arguments[G]:{};G%2?ae(Object(z),!0).forEach(function(K){(0,v.default)(L,K,z[K])}):r?_(L,r(z)):ae(Object(z)).forEach(function(K){s(L,K,I(z,K))})}return L}var q=E.default.Item,ve=P.default.Group,Ce={labelCol:{fixedSpan:6},wrapperCol:{span:18}},ge=function(G){var z=G.dataSource,K=G.records,Q=G.onClose,j=G.fetchAdd,w=(0,M.useDispatch)(),_e=(0,R.useState)([]),ce=(0,m.default)(_e,2),Ee=ce[0],xe=ce[1],ye=p.default.useField(),Me=ye.init,Ne=(0,R.useState)({appType:1}),Ae=(0,m.default)(Ne,2),V=Ae[0],Be=Ae[1];(0,R.useEffect)(function(){var De=function(){var Pe=(0,t.default)(regeneratorRuntime.mark(function se(){var b,$,pe,Z,ee,re,fe,We,Te;return regeneratorRuntime.wrap(function(me){for(;;)switch(me.prev=me.next){case 0:return Z=(b=($=(pe=(0,T.getParams)("ns"))!==null&&pe!==void 0?pe:window.curNamespace)!==null&&$!==void 0?$:(0,U.getCookie)("curNamespace"))!==null&&b!==void 0?b:"default",ee=V.AppName,re={namespace:Z,region:(0,T.getParams)("region"),appType:"0"},ee&&(re.app_id=ee),me.next=6,w.agentSetting.getGetUserApplicationGroups({args:(0,i.default)(re)});case 6:fe=me.sent,We=fe.Data,Te=We===void 0?[]:We,xe(Te);case 10:case"end":return me.stop()}},se)}));return function(){return Pe.apply(this,arguments)}}();V!=null&&V.AppName&&(V==null?void 0:V.appType)===1&&(xe([]),De())},[V==null?void 0:V.AppName]);var Le=function(){ye.validate(function(Pe,se){var b=se.appType,$=se.AppName,pe=se.cAppName,Z=se.AppGroupName,ee=se.cAppGroupName,re=b===1?$:pe,fe=b===1?Z:ee;re&&fe&&Ue(z,K,re,fe)})},Ue=function(Pe,se,b,$){var pe=D.default.cloneDeep(Pe),Z=[];se.forEach(function(ee){Z.push(ee.instanceId)}),pe.forEach(function(ee){Z.indexOf(ee.instanceId)!==-1&&(ee.pluginStatus=1)}),j(pe,Z,b,$),Q()};return R.default.createElement(l.default,{visible:!0,title:O.default.t("Install the probe").toString(),style:{minWidth:"500px"},okProps:{children:O.default.t("Install").toString()},onCancel:Q,onClose:Q,onOk:Le,locale:(0,W.default)().Dialog},R.default.createElement("div",{className:F.default.content},R.default.createElement(E.default,(0,h.default)({},Ce,{field:ye,onChange:function(Pe){return Be(Pe)}}),R.default.createElement(q,{label:O.default.t("Application").toString()},R.default.createElement(ve,{name:"appType",defaultValue:V.appType},R.default.createElement(P.default,{value:1},R.default.createElement(S.default,null,"Existing application")),R.default.createElement(P.default,{value:2},R.default.createElement(S.default,null,"Add application")))),R.default.createElement(q,{label:O.default.t("Application Name").toString(),required:!0},V.appType!==1&&R.default.createElement(a.default,(0,h.default)({placeholder:O.default.t("Please input application name").toString(),type:"text",maxLength:60,showLimitHint:!0},Me("cAppName",{rules:[{required:!0,message:O.default.t("Please enter a valid name").toString()},{pattern:/^[\w|\||-]*$/g,message:O.default.t("Please enter a valid name").toString()}]})))||R.default.createElement(B.default,{params:{filterDisabled:!0,appType:0},placeholder:O.default.t("Please select an app name").toString(),name:"AppName",value:V.AppName,onChange:function(Pe,se,b){Be(le(le({},V),{},{AppName:b.label}))}})),R.default.createElement(q,{label:O.default.t("Group Name").toString(),required:!0},V.appType!==1&&R.default.createElement(a.default,(0,h.default)({placeholder:O.default.t("Please enter a group name"),type:"text",maxLength:60,showLimitHint:!0,trim:!0},Me("cAppGroupName",{rules:[{pattern:/^[\w|\||-]*$/g,message:O.default.t("Please enter a valid name").toString()}]})))||R.default.createElement(d.default,{name:"AppGroupName",style:{width:"100%"},dataSource:Ee,placeholder:O.default.t("Please select a specific application group"),locale:(0,W.default)().Select})))))},oe=(0,R.memo)(ge);n.default=oe})},15179:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(93484),e(72153),e(42499),e(17225),e(17534),e(81853),e(57379),e(61253),e(31562),e(9644),e(27378),e(66697),e(60042),e(14798),e(68055),e(43858),e(73262)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n,i,l,d,v,a,h,t,p,m,P,E,R,S){"use strict";var D=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=D(g),r=D(r),_=D(_),n=D(n),i=D(i),l=D(l),d=D(d),v=F(v),a=D(a),h=D(h),t=F(t),p=D(p),m=D(m),P=D(P),E=D(E),R=D(R);var O;function W(B){if(typeof c!="function")return null;var N=new c,X=new c;return(W=function(ae){return ae?X:N})(B)}function F(B,N){if(!N&&B&&B.__esModule)return B;if(B===null||A(B)!=="object"&&typeof B!="function")return{default:B};var X=W(N);if(X&&X.has(B))return X.get(B);var J={},ae=s&&I;for(var le in B)if(le!=="default"&&Object.prototype.hasOwnProperty.call(B,le)){var q=ae?I(B,le):null;q&&(q.get||q.set)?s(J,le,q):J[le]=B[le]}return J.default=B,X&&X.set(B,J),J}var U=(0,m.default)((O={},(0,d.default)(O,R.default.v_a_b,!0),(0,d.default)(O,R.default.mr10,!0),(0,d.default)(O,R.default.red,!0),O)),T=function(N){var X=N.totalCount,J=N.pageSize,ae=N.isLoading,le=N.dataSource,q=N.handleOnPageSizeChange,ve=N.handlePaginationChange,Ce=N.fetchAdd,ge=(0,t.useState)([]),oe=(0,l.default)(ge,2),L=oe[0],G=oe[1],z=(0,t.useState)(!1),K=(0,l.default)(z,2),Q=K[0],j=K[1],w=(0,t.useState)(!1),_e=(0,l.default)(w,2),ce=_e[0],Ee=_e[1],xe=(0,t.useState)(NaN),ye=(0,l.default)(xe,2),Me=ye[0],Ne=ye[1],Ae=(0,t.useRef)([]),V=function($){G($),j(!0)},Be=function($){Ee(!ce),Ne($&&$.osType)},Le=function($,pe){Ae.current=pe.filter(function(Z){return Z.pluginStatus!==2})},Ue=function($){return{disabled:!$.canAutoInstall}},De=function(){if(!Ae.current.length){i.default.warning({content:P.default.t("Please select a machine before installing").toString(),duration:4e3});return}V(Ae.current)},Pe=function($){if($===S.OS_TYPE.LINUX)return"linux"},se=function($,pe,Z){var ee,re;if(Z.networkType==="classic")return t.default.createElement("span",null,t.default.createElement(p.default,null,"Not currently supported"));switch(Z.pluginStatus){case 0:re=t.default.createElement(t.default.Fragment,null,t.default.createElement(v.LinkButton,{onClick:function(){return V([Z])}},t.default.createElement(p.default,null,"Click install")),t.default.createElement(v.LinkButton,{onClick:function(){return Be(Z)}},t.default.createElement(p.default,null,"Manual installation")));break;case 1:re=t.default.createElement("span",null,t.default.createElement("span",{className:R.default.mr10},t.default.createElement(p.default,null,"Installing")),t.default.createElement(n.default,{type:"loading",size:"small"}));break;case 2:re=t.default.createElement("span",null,t.default.createElement("span",{className:(0,m.default)((ee={},(0,d.default)(ee,R.default.v_a_b,!0),(0,d.default)(ee,R.default.mr10,!0),ee)),style:{color:"green"}},t.default.createElement(p.default,null,"Successful installation")),t.default.createElement(n.default,{type:"success",size:"small"}));break;case 3:re=t.default.createElement(t.default.Fragment,null,t.default.createElement(v.LinkButton,{onClick:function(){return V([Z])}},t.default.createElement(p.default,null,"Click install")),t.default.createElement(v.LinkButton,{onClick:function(){return Be(Z)}},t.default.createElement(p.default,null,"Manual installation")));break;case-1:re=t.default.createElement(t.default.Fragment,null,t.default.createElement("span",null,t.default.createElement("span",{className:U},t.default.createElement(p.default,null,"Installation failed")),t.default.createElement(n.default,{type:"error",size:"small"})),t.default.createElement(v.LinkButton,{onClick:function(){return V([Z])}},t.default.createElement(p.default,null,"Retry")),t.default.createElement(v.LinkButton,{onClick:function(){return Be(Z)}},t.default.createElement(p.default,null,"Manual installation")));break;case 999:re=t.default.createElement(t.default.Fragment,null,t.default.createElement("span",null,t.default.createElement("span",{className:U},t.default.createElement(p.default,null,"Installation timed out")),t.default.createElement(n.default,{type:"error",size:"small"})),t.default.createElement(v.LinkButton,{onClick:function(){return V([Z])}},t.default.createElement(p.default,null,"Retry")),t.default.createElement(v.LinkButton,{onClick:function(){return Be(Z)}},t.default.createElement(p.default,null,"Manual installation")));break;default:break}return t.default.createElement(v.default,{style:{justifyContent:"center"}},re)};return t.default.createElement(t.default.Fragment,null,t.default.createElement(_.default,{className:R.default.content,dataSource:le,loading:ae,primaryKey:"instanceId",rowSelection:{onChange:Le,getProps:Ue},hasBorder:!1,locale:(0,E.default)().Table},t.default.createElement(_.default.Column,{title:P.default.t("Instance name/hostname").toString(),dataIndex:"instanceName",align:"center"}),t.default.createElement(_.default.Column,{title:P.default.t("Plugin type").toString(),dataIndex:"pluginType",align:"center"}),t.default.createElement(_.default.Column,{title:P.default.t("Operating system").toString(),dataIndex:"osType",align:"center",cell:Pe}),t.default.createElement(_.default.Column,{title:"IP",dataIndex:"ip",align:"center"}),t.default.createElement(_.default.Column,{title:P.default.t("Plugin status").toString(),dataIndex:"pluginStatusShow",align:"center"}),t.default.createElement(_.default.Column,{title:P.default.t("Operation").toString(),cell:se,align:"center"})),t.default.createElement("div",{className:R.default.footer},t.default.createElement(r.default,{size:"small",style:{marginLeft:"45px"},onClick:De},t.default.createElement(p.default,null,"Bulk install")),t.default.createElement(g.default,{total:X,defaultCurrent:1,onChange:ve,className:R.default.pagination,pageSize:J,onPageSizeChange:q,pageSizeSelector:"dropdown",pageSizeList:[10,20,50,100],locale:(0,E.default)().Pagination})),Q&&t.default.createElement(a.default,{dataSource:le,records:L,onClose:function(){return j(!1)},fetchAdd:Ce}),ce&&t.default.createElement(h.default,{isInstall:!0,onClose:function(){return Ee(!1)},ostype:Me}))},M=(0,t.memo)(T);C.default=M})},52247:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587),C=e(83452),g=e(95315),r=e(63774),_=e(92937);(function(n,i){if(!0)!(y=[f,e(83452),e(35049),e(57379),e(77809),e(81853),e(91769),e(27378),e(15179),e(14798),e(14870)],u=i,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var l})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(n,i,l,d,v,a,h,t,p,m,P){"use strict";var E=e(67971);s(n,"__esModule",{value:!0}),n.default=void 0,i=E(i),l=E(l),d=E(d),v=E(v),a=E(a),h=E(h),t=S(t),p=E(p),m=E(m);function R(U){if(typeof c!="function")return null;var T=new c,M=new c;return(R=function(N){return N?M:T})(U)}function S(U,T){if(!T&&U&&U.__esModule)return U;if(U===null||A(U)!=="object"&&typeof U!="function")return{default:U};var M=R(T);if(M&&M.has(U))return M.get(U);var B={},N=s&&I;for(var X in U)if(X!=="default"&&Object.prototype.hasOwnProperty.call(U,X)){var J=N?I(U,X):null;J&&(J.get||J.set)?s(B,X,J):B[X]=U[X]}return B.default=U,M&&M.set(U,B),B}function D(U,T){var M=C(U);if(g){var B=g(U);T&&(B=B.filter(function(N){return I(U,N).enumerable})),M.push.apply(M,B)}return M}function O(U){for(var T=1;T<arguments.length;T++){var M=arguments[T]!=null?arguments[T]:{};T%2?D(Object(M),!0).forEach(function(B){(0,d.default)(U,B,M[B])}):r?_(U,r(M)):D(Object(M)).forEach(function(B){s(U,B,I(M,B))})}return U}var W=function(){var T=(0,P.useDispatch)(),M=(0,t.useState)([]),B=(0,a.default)(M,2),N=B[0],X=B[1],J=(0,t.useState)(!1),ae=(0,a.default)(J,2),le=ae[0],q=ae[1],ve=(0,t.useState)(1),Ce=(0,a.default)(ve,2),ge=Ce[0],oe=Ce[1],L=(0,t.useState)(10),G=(0,a.default)(L,2),z=G[0],K=G[1],Q=(0,t.useState)(0),j=(0,a.default)(Q,2),w=j[0],_e=j[1],ce=(0,t.useState)(""),Ee=(0,a.default)(ce,2),xe=Ee[0],ye=Ee[1],Me=(0,t.useState)([]),Ne=(0,a.default)(Me,2),Ae=Ne[0],V=Ne[1],Be=(0,t.useState)(""),Le=(0,a.default)(Be,2),Ue=Le[0],De=Le[1],Pe=(0,t.useState)([]),se=(0,a.default)(Pe,2),b=se[0],$=se[1],pe=(0,t.useState)([]),Z=(0,a.default)(pe,2),ee=Z[0],re=Z[1],fe=(0,t.useState)([]),We=(0,a.default)(fe,2),Te=We[0],He=We[1],me=(0,t.useRef)({}),Xe=(0,t.useRef)(0),Ge=(0,t.useCallback)(function(){var te=(0,v.default)(regeneratorRuntime.mark(function Y(ue){var H,k,ne,ie,Re,he,Oe,Se;return regeneratorRuntime.wrap(function(de){for(;;)switch(de.prev=de.next){case 0:return de.prev=0,q(!0),H=Ae.join(","),k=(0,d.default)({},Ue,H),de.next=6,T.agentSetting.getQueryWaitInstallPlugin(O({PageNumber:ue||ge,PageSize:z},k));case 6:ne=de.sent,ie=ne.Data,Re=ie===void 0?{}:ie,he=Re,Oe=he.totalItem,Se=he.result,Ye(Se),_e(Oe),q(!1),de.next=18;break;case 15:de.prev=15,de.t0=de.catch(0),q(!1);case 18:case"end":return de.stop()}},Y,null,[[0,15]])}));return function(Y){return te.apply(this,arguments)}}(),[ge,Ae,Ue,z]),Ye=function(Y){var ue=[];Y.forEach(function(H){var k,ne="",ie;H.pluginType==="CHAOS_AGENT"?ie=m.default.t("Public").toString():ie=m.default.t("HostMachine").toString(),H.publicIp&&H.privateIp?k=t.default.createElement("span",null,H.publicIp+ie,t.default.createElement("br",null),H.privateIp+m.default.t("Private").toString()):!H.publicIp&&H.privateIp?k=t.default.createElement("span",null,H.privateIp+m.default.t("Private").toString()):H.publicIp&&!H.privateIp&&(k=t.default.createElement("span",null," ",H.publicIp+ie));switch(H.pluginStatus){case 0:ne=m.default.t("To be installed").toString();break;case 1:ne=m.default.t("Installing").toString();break;case-1:ne=m.default.t("Installation failed").toString();break;case 2:ne=m.default.t("Online").toString();break;case 3:ne=m.default.t("Offline").toString();break;case 4:ne=m.default.t("Uninstalling").toString();break;case 5:ne=m.default.t("Uninstall failed").toString();break;default:break}ue.push({instanceId:H.instanceId,instanceName:H.instanceName,pluginStatus:H.pluginStatus,pluginStatusShow:ne,ip:k,pluginType:H.pluginType,canAutoInstall:H.canAutoInstall,osType:H.osType,networkType:H.networkType,configurationId:"xxx",enable:!1})}),X(ue)};(0,t.useEffect)(function(){Ge()},[Ge]);var ze=function(Y){ye(Y)},Qe=function(Y,ue){var H=(0,l.default)(Ae);if(!Y)return;if(ue==="InstanceNameList"&&Ae.length===1)return;H.includes(Y)||H.push(Y),V(H),De(ue),ye("")},Ve=function(){V([]),ye("")},Ze=function(Y,ue){var H=(0,l.default)(Ae),k=ue.getAttribute("data-id")||"",ne=H.filter(function(ie){return ie!==k});return V(ne),!0},Je=function(){var te=(0,v.default)(regeneratorRuntime.mark(function Y(ue,H,k,ne){var ie,Re,he,Oe,Se,Ke;return regeneratorRuntime.wrap(function(Ie){for(;;)switch(Ie.prev=Ie.next){case 0:return X(ue),Ie.next=3,T.agentSetting.getBatchInstallPlugin({InstanceIds:H.join(","),AppName:k,AppGroupName:ne});case 3:ie=Ie.sent,Re=ie.Data,he=Re===void 0?{}:Re,he&&(Oe=he,Se=[],Ke=[],(0,i.default)(Oe).forEach(function(Fe){Oe[Fe]?Se.push(Fe):Ke.push(Fe)}),$(H),re(Se),He(Ke));case 7:case"end":return Ie.stop()}},Y)}));return function(ue,H,k,ne){return te.apply(this,arguments)}}(),we=function(){var te=(0,v.default)(regeneratorRuntime.mark(function Y(ue,H,k){var ne,ie,Re,he,Oe,Se;return regeneratorRuntime.wrap(function(de){for(;;)switch(de.prev=de.next){case 0:return de.next=2,T.agentSetting.getBatchQueryPluginStatus({Loop:!0,InstanceIds:H});case 2:ne=de.sent,ie=ne.Data,Re=ie===void 0?{}:ie,Re&&(he=Re,Oe=(0,l.default)(ue),Se=0,(0,i.default)(he).forEach(function(Ie){Oe.forEach(function(Fe){Ie===Fe.instanceId&&(Fe.pluginStatus=he[Ie])}),he[Ie]===2&&Se++}),X(Oe),Se===(0,i.default)(he).length&&(clearInterval(me.current[k]),me.current[k]=null,re([]),He([])));case 6:case"end":return de.stop()}},Y)}));return function(ue,H,k){return te.apply(this,arguments)}}(),be=function(Y){clearInterval(me.current[Y]),me.current[Y]=null,re([]),He([]),je(999)},je=function(Y){var ue=(0,l.default)(N),H=Y||-1;ue.forEach(function(k){Te.indexOf(k.instanceId)!==-1&&(k.pluginStatus===1&&(k.pluginStatus=H))}),X(ue)};(0,t.useEffect)(function(){return ee.length&&(function(te,Y){me.current[Xe.current]=setInterval(function(){we(Y,ee.join(","),te)},2e3)}(Xe.current,N),setTimeout(function(){be(Xe.current)},61e3),Xe.current++),Te.length&&je(),function(){(0,i.default)(me.current).forEach(function(te){clearInterval(me.current[+te])})}},[ee,Te,b,N]);var $e=function(Y){oe(Y)},ke=function(Y){K(Y)};return t.default.createElement(t.default.Fragment,null,t.default.createElement(h.default,{filterText:xe,searchKeywordList:Ae,searchFilterKey:Ue,handleSearchChange:ze,handleFilterSearch:Qe,handleFilterSearchChange:Ve,tagSearchClose:Ze,clearFilter:function(){return V([])}}),t.default.createElement(p.default,{totalCount:w,pageSize:z,isLoading:le,dataSource:N,handleOnPageSizeChange:ke,handlePaginationChange:$e,fetchAdd:Je}))},F=(0,t.memo)(W);n.default=F})},84589:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(91714),e(17225),e(17534),e(77809),e(81853),e(27378),e(66697),e(36012),e(14798),e(22e3),e(14870)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n,i,l,d,v,a,h,t){"use strict";var p=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=p(g),r=p(r),_=p(_),n=p(n),i=p(i),l=P(l),d=p(d),v=p(v),a=p(a),h=p(h);function m(S){if(typeof c!="function")return null;var D=new c,O=new c;return(m=function(F){return F?O:D})(S)}function P(S,D){if(!D&&S&&S.__esModule)return S;if(S===null||A(S)!=="object"&&typeof S!="function")return{default:S};var O=m(D);if(O&&O.has(S))return O.get(S);var W={},F=s&&I;for(var U in S)if(U!=="default"&&Object.prototype.hasOwnProperty.call(S,U)){var T=F?I(S,U):null;T&&(T.get||T.set)?s(W,U,T):W[U]=S[U]}return W.default=S,O&&O.set(S,W),W}var E=function(){var D=(0,t.useDispatch)(),O=(0,l.useState)(""),W=(0,i.default)(O,2),F=W[0],U=W[1],T=(0,l.useState)(""),M=(0,i.default)(T,2),B=M[0],N=M[1],X=(0,l.useState)(""),J=(0,i.default)(X,2),ae=J[0],le=J[1];(0,l.useEffect)(function(){q("v2"),q("v3"),Ce()},[]);function q(K){return ve.apply(this,arguments)}function ve(){return ve=(0,n.default)(regeneratorRuntime.mark(function K(Q){var j,w,_e;return regeneratorRuntime.wrap(function(Ee){for(;;)switch(Ee.prev=Ee.next){case 0:return Ee.next=2,D.agentSetting.getQueryUninstallAndInstallCommand("QueryInstallCommand",{Mode:"k8s_helm",helmVersion:Q});case 2:j=Ee.sent,w=j.Data,_e=w===void 0?{}:w,Q==="v2"?U(_e&&_e.command_install):N(_e&&_e.command_install);case 6:case"end":return Ee.stop()}},K)})),ve.apply(this,arguments)}function Ce(){return ge.apply(this,arguments)}function ge(){return ge=(0,n.default)(regeneratorRuntime.mark(function K(){var Q,j,w;return regeneratorRuntime.wrap(function(ce){for(;;)switch(ce.prev=ce.next){case 0:return ce.next=2,D.agentSetting.getQueryHelmPackageAddress();case 2:Q=ce.sent,j=Q.Data,w=j===void 0?"":j,le(w);case 6:case"end":return ce.stop()}},K)})),ge.apply(this,arguments)}function oe(K,Q){if(!K&&Q){(0,v.default)(Q),_.default.success(a.default.t("Copy successfully"));return}var j=K==="v2"?F:B;(0,v.default)(j),_.default.success(a.default.t("Copy successfully"))}var L=(0,l.useMemo)(function(){var K=ae.split(" "),Q=(0,i.default)(K,2),j=Q[1],w=j===void 0?"":j;return l.default.createElement("div",null,l.default.createElement("div",null,l.default.createElement("p",null,l.default.createElement(d.default,null,"Helm chart package"),l.default.createElement("a",{href:w==null?void 0:w.replace(/http:\/\//,"https://")},l.default.createElement(d.default,null,"Manual download"))),l.default.createElement("p",null,l.default.createElement(d.default,null,"Or")),l.default.createElement("p",null,l.default.createElement("div",{className:h.default.codeBox},l.default.createElement("p",null,ae),l.default.createElement("div",{className:h.default.codeCopy,onClick:function(){return oe(void 0,ae)}},l.default.createElement(r.default,{type:"copy",className:h.default.copyIcon}))))))},[ae]),G=(0,l.useMemo)(function(){return l.default.createElement(l.default.Fragment,null,l.default.createElement("div",null,l.default.createElement("p",null,l.default.createElement(d.default,null,"Helm v2 install probe")),l.default.createElement("div",{className:h.default.codeBox},l.default.createElement("p",null,F),l.default.createElement("div",{className:h.default.codeCopy,onClick:function(){return oe("v2")}},l.default.createElement(r.default,{type:"copy",className:h.default.copyIcon})))),l.default.createElement("div",null,l.default.createElement("p",null,l.default.createElement(d.default,null,"Helm v3 install probe")),l.default.createElement("div",{className:h.default.codeBox},l.default.createElement("p",null,B),l.default.createElement("div",{className:h.default.codeCopy,onClick:function(){return oe("v3")}},l.default.createElement(r.default,{type:"copy",className:h.default.copyIcon})))))},[F,B]),z=(0,l.useMemo)(function(){var K=[{title:a.default.t("Step one").toString(),content:L},{title:a.default.t("Step two").toString(),content:G}];return K.map(function(Q,j){return l.default.createElement(g.default.Item,{status:"process",key:j,title:Q.title,content:Q.content})})},[L,G]);return l.default.createElement("div",{className:h.default.content},l.default.createElement(g.default,{direction:"ver",shape:"circle",animation:!1,readOnly:!0},z))},R=(0,l.memo)(E);C.default=R})},2470:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(17534),e(77809),e(81853),e(27378),e(66697),e(36012),e(14798),e(8541),e(14870)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n,i,l,d,v,a){"use strict";var h=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=h(g),r=h(r),_=h(_),n=p(n),i=h(i),l=h(l),d=h(d),v=h(v);function t(E){if(typeof c!="function")return null;var R=new c,S=new c;return(t=function(O){return O?S:R})(E)}function p(E,R){if(!R&&E&&E.__esModule)return E;if(E===null||A(E)!=="object"&&typeof E!="function")return{default:E};var S=t(R);if(S&&S.has(E))return S.get(E);var D={},O=s&&I;for(var W in E)if(W!=="default"&&Object.prototype.hasOwnProperty.call(E,W)){var F=O?I(E,W):null;F&&(F.get||F.set)?s(D,W,F):D[W]=E[W]}return D.default=E,S&&S.set(E,D),D}var m=function(){var R=(0,a.useDispatch)(),S=(0,n.useState)(""),D=(0,_.default)(S,2),O=D[0],W=D[1];(0,n.useEffect)(function(){(0,r.default)(regeneratorRuntime.mark(function U(){var T,M,B;return regeneratorRuntime.wrap(function(X){for(;;)switch(X.prev=X.next){case 0:return X.next=2,R.agentSetting.getQueryUninstallAndInstallCommand("QueryInstallCommand",{Mode:"host",OsType:0});case 2:T=X.sent,M=T.Data,B=M===void 0?{command_install:""}:M,W(B&&B.command_install);case 6:case"end":return X.stop()}},U)}))()},[]);var F=function(){(0,l.default)(O),g.default.success(d.default.t("Copy successfully"))};return n.default.createElement("div",{style:{marginTop:"20px"}},n.default.createElement("h1",null,n.default.createElement(i.default,null,"Linux host")),n.default.createElement("div",{className:v.default.content},n.default.createElement("p",{style:{fontSize:"16px"}},n.default.createElement(i.default,null,"Log in to the host and use the root user to execute the following command")),n.default.createElement("pre",{style:{whiteSpace:"pre-wrap",wordWrap:"break-word"}},O),n.default.createElement("a",{onClick:F,style:{textDecoration:"none"}},n.default.createElement(i.default,null,"copy the command"))))},P=(0,n.memo)(m);C.default=P})},23241:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(52247),e(84589),e(2470),e(27378)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n){"use strict";var i=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=i(g),r=i(r),_=i(_),n=d(n);function l(m){if(typeof c!="function")return null;var P=new c,E=new c;return(l=function(S){return S?E:P})(m)}function d(m,P){if(!P&&m&&m.__esModule)return m;if(m===null||A(m)!=="object"&&typeof m!="function")return{default:m};var E=l(P);if(E&&E.has(m))return E.get(m);var R={},S=s&&I;for(var D in m)if(D!=="default"&&Object.prototype.hasOwnProperty.call(m,D)){var O=S?I(m,D):null;O&&(O.get||O.set)?s(R,D,O):R[D]=m[D]}return R.default=m,E&&E.set(m,R),R}var v="ecs",a="k8s",h="public",t=function(P){var E=P.installMode;return n.default.createElement("div",{style:{marginBottom:"8px"}},E===v&&n.default.createElement(g.default,null),E===a&&n.default.createElement(r.default,null),E===h&&n.default.createElement(_.default,null))},p=(0,n.memo)(t);C.default=p})},7659:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(38856),e(27378),e(66697),e(88460)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n){"use strict";var i=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=i(g),r=d(r),_=i(_),n=i(n);function l(p){if(typeof c!="function")return null;var m=new c,P=new c;return(l=function(R){return R?P:m})(p)}function d(p,m){if(!m&&p&&p.__esModule)return p;if(p===null||A(p)!=="object"&&typeof p!="function")return{default:p};var P=l(m);if(P&&P.has(p))return P.get(p);var E={},R=s&&I;for(var S in p)if(S!=="default"&&Object.prototype.hasOwnProperty.call(p,S)){var D=R?I(p,S):null;D&&(D.get||D.set)?s(E,S,D):E[S]=p[S]}return E.default=p,P&&P.set(p,E),E}var v="k8s",a="public",h=function(m){var P=m.enable,E=m.handleSelectEnv,R=function(D){P&&E(D)};return r.default.createElement("div",{className:n.default.content},r.default.createElement("h1",null,r.default.createElement(_.default,null,"Please select the environment you want to install")),r.default.createElement("div",{className:n.default.container},r.default.createElement("ul",null,r.default.createElement("li",{onClick:function(){return R(a)}},r.default.createElement(g.default,{className:n.default["install-card"]},r.default.createElement("span",{className:n.default["install-card-text"]},r.default.createElement("span",null,r.default.createElement(_.default,null,"Host"),r.default.createElement("div",{className:n.default.txc,style:{marginTop:"10px",display:"flex",justifyContent:"center"}},r.default.createElement("img",{src:"data:image/png;base64,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",alt:"",style:{width:64,height:64}})))))),r.default.createElement("li",{onClick:function(){return R(v)}},r.default.createElement(g.default,{className:P?n.default["install-card"]:n.default["card-disable"]},r.default.createElement("span",{className:n.default["install-card-text"]},r.default.createElement("span",null,r.default.createElement(_.default,null,"Self-built Kubernetes"),r.default.createElement("div",{className:n.default.txc,style:{marginTop:"10px"}},r.default.createElement("img",{src:"data:image/png;base64,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",alt:""})))))))))},t=(0,r.memo)(h);C.default=t})},41260:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(91714),e(27378),e(14798)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_){"use strict";var n=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=n(g),r=l(r),_=n(_);function i(a){if(typeof c!="function")return null;var h=new c,t=new c;return(i=function(m){return m?t:h})(a)}function l(a,h){if(!h&&a&&a.__esModule)return a;if(a===null||A(a)!=="object"&&typeof a!="function")return{default:a};var t=i(h);if(t&&t.has(a))return t.get(a);var p={},m=s&&I;for(var P in a)if(P!=="default"&&Object.prototype.hasOwnProperty.call(a,P)){var E=m?I(a,P):null;E&&(E.get||E.set)?s(p,P,E):p[P]=a[P]}return p.default=a,t&&t.set(a,p),p}var d=function(h){var t=h.step,p=h.handleSelectStep;return r.default.createElement("div",{style:{marginBottom:"8px"}},r.default.createElement(g.default,{current:t,shape:"arrow"},r.default.createElement(g.default.Item,{title:_.default.t("Select environment").toString(),onClick:function(){return p(0)}}),r.default.createElement(g.default.Item,{title:_.default.t("Install the Application High Availability Plugin").toString(),onClick:function(){return p(1)},disabled:t<1})))},v=(0,r.memo)(d);C.default=v})},29443:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(17225),e(81853),e(64780),e(27378),e(23241),e(7659),e(41260),e(66697),e(4345),e(99328),e(14870),e(42058)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_,n,i,l,d,v,a,h,t,p){"use strict";var m=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=m(g),r=m(r),_=m(_),n=E(n),i=m(i),l=m(l),d=m(d),v=m(v),a=m(a);function P(O){if(typeof c!="function")return null;var W=new c,F=new c;return(P=function(T){return T?F:W})(O)}function E(O,W){if(!W&&O&&O.__esModule)return O;if(O===null||A(O)!=="object"&&typeof O!="function")return{default:O};var F=P(W);if(F&&F.has(O))return F.get(O);var U={},T=s&&I;for(var M in O)if(M!=="default"&&Object.prototype.hasOwnProperty.call(O,M)){var B=T?I(O,M):null;B&&(B.get||B.set)?s(U,M,B):U[M]=O[M]}return U.default=O,F&&F.set(O,U),U}var R="ecs",S=function(){var W=(0,t.useDispatch)(),F=(0,p.useHistory)(),U=(0,n.useState)(0),T=(0,r.default)(U,2),M=T[0],B=T[1],N=(0,n.useState)(R),X=(0,r.default)(N,2),J=X[0],ae=X[1];(0,n.useEffect)(function(){W.pageHeader.setTitle(""),W.pageHeader.showBackArrow(!1)},[]);var le=function(L){var G=L?M+1:M-1;B(G)},q=function(){var L=location||"",G=L.pathname;/\/chaos\/agentmanage/.test(G)?(0,h.pushUrl)(F,"/chaos/experiment/scope/control"):(0,h.pushUrl)(F,G.replace("/step","")),(0,h.removeParams)("iis")},ve=function(L){L<M&&B(L)},Ce=function(L){ae(L),B(1)};function ge(){var oe=location||"",L=oe.pathname;/\/chaos\/agentmanage/.test(L)?(0,h.pushUrl)(F,"/chaos/experiment/scope/control"):(0,h.pushUrl)(F,L.replace("/step",""))}return n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:a.default.headTop,onClick:function(){return ge()}},n.default.createElement(g.default,{type:"arrow-alt-left"}),"\xA0",n.default.createElement("span",{className:a.default.protectionAccess},n.default.createElement(v.default,null,"Install the probe"))),n.default.createElement(d.default,{step:M,handleSelectStep:ve}),M===0&&n.default.createElement(l.default,{enable:!0,handleSelectEnv:Ce}),M===1&&n.default.createElement(i.default,{installMode:J}),n.default.createElement(_.default,{step:M,handleComplete:q,handlePrevOrNextStep:le}))},D=(0,n.memo)(S);C.default=D})},5311:function(x,f,e){var u,y,o,A=e(24596),s=e(67394),c=e(93168),I=e(23587);(function(C,g){if(!0)!(y=[f,e(29443),e(27378),e(99328)],u=g,o=typeof u=="function"?u.apply(f,y):u,o!==void 0&&(x.exports=o));else var r})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(C,g,r,_){"use strict";var n=e(67971);s(C,"__esModule",{value:!0}),C.default=void 0,g=n(g),r=l(r);function i(a){if(typeof c!="function")return null;var h=new c,t=new c;return(i=function(m){return m?t:h})(a)}function l(a,h){if(!h&&a&&a.__esModule)return a;if(a===null||A(a)!=="object"&&typeof a!="function")return{default:a};var t=i(h);if(t&&t.has(a))return t.get(a);var p={},m=s&&I;for(var P in a)if(P!=="default"&&Object.prototype.hasOwnProperty.call(a,P)){var E=m?I(a,P):null;E&&(E.get||E.set)?s(p,P,E):p[P]=a[P]}return p.default=a,t&&t.set(a,p),p}var d=function(){var h=(0,_.getParams)("iis")==="1";return(0,r.useEffect)(function(){return function(){return(0,_.removeParams)("iis")}},[]),h?r.default.createElement(g.default,null):null},v=(0,r.memo)(d);C.default=v})},22888:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__2pl1T {
  margin-bottom: 8px;
}
  .index__content__2pl1T .next-search {
    width: auto;
  }
  .index__content__2pl1T .index__info__pmsma {
    color: rgb(170, 170, 170);
    vertical-align: top;
    line-height: 35px;
    margin-left: 10px
  }`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingChaosAgent/Ecs/HeadHandler/index.css"],names:[],mappings:"AAAA;EACE,kBAAkB;AAUpB;EATE;IACE,WAAW;EACb;EACA;IACE,yBAAyB;IACzB,mBAAmB;IACnB,iBAAiB;IACjB;EACF",sourcesContent:[`.content {
  margin-bottom: 8px;
  :global(.next-search) {
    width: auto;
  }
  .info {
    color: rgb(170, 170, 170);
    vertical-align: top;
    line-height: 35px;
    margin-left: 10px
  }
}`],sourceRoot:""}]),s.locals={content:"index__content__2pl1T",info:"index__info__pmsma"};const c=s},74560:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__gzZE4 dl {
    display: flex;
    align-items: baseline;
  }
    .index__content__gzZE4 dl dt {
      width: 15%;
      font-weight: normal;
      color: #555555;
      font-size: 12px;
    }
    .index__content__gzZE4 dl dd {
      width: 85%;
    }
    .index__content__gzZE4 dl dd .next-select {
        width: 100%;
      }`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingChaosAgent/Ecs/TableList/InstallDialog/index.css"],names:[],mappings:"AACE;IACE,aAAa;IACb,qBAAqB;EAavB;IAZE;MACE,UAAU;MACV,mBAAmB;MACnB,cAAc;MACd,eAAe;IACjB;IACA;MACE,UAAU;IAIZ;IAHE;QACE,WAAW;MACb",sourcesContent:[`.content {
  dl {
    display: flex;
    align-items: baseline;
    dt {
      width: 15%;
      font-weight: normal;
      color: #555555;
      font-size: 12px;
    }
    dd {
      width: 85%;
      :global(.next-select) {
        width: 100%;
      }
    }
  }
}`],sourceRoot:""}]),s.locals={content:"index__content__gzZE4"};const c=s},95674:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__UsbFa .next-table-header .next-checkbox-wrapper {
      position: absolute;
      bottom: -30px;
    }
  .index__content__UsbFa .index__ml10__gbbXx {
    margin-left: 10px;
  }
  .index__content__UsbFa .index__mr10__\\+Xfio {
    margin-right: 10px;
  }
  .index__content__UsbFa .index__red__fZRrX {
    color: red;
  }
  .index__content__UsbFa .index__v_a_b__vt0w- {
    vertical-align: bottom;
  }

.index__footer__d3Vuj {
  display: flex;
  justify-content: space-between;
  border: 1px solid #ddd;
  height: 40px;
  align-items: center;
  border-top: 0;
  padding: 0px 4px;
}

.index__footer__d3Vuj .index__pagination__eCSn0 {
    display: flex;
    justify-content: flex-end;
  }

.index__footer__d3Vuj .index__pagination__eCSn0 .next-pagination-pages {
      margin-left: 8px;
    }

`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingChaosAgent/Ecs/TableList/index.css"],names:[],mappings:"AAEI;MACE,kBAAkB;MAClB,aAAa;IACf;EAEF;IACE,iBAAiB;EACnB;EAEA;IACE,kBAAkB;EACpB;EAEA;IACE,UAAU;EACZ;EAEA;IACE,sBAAsB;EACxB;;AAIF;EACE,aAAa;EACb,8BAA8B;EAC9B,sBAAsB;EACtB,YAAY;EACZ,mBAAmB;EACnB,aAAa;EACb,gBAAgB;AAQlB;;AAPE;IACE,aAAa;IACb,yBAAyB;EAI3B;;AAHE;MACE,gBAAgB;IAClB",sourcesContent:[`.content {
  :global(.next-table-header) {
    :global(.next-checkbox-wrapper) {
      position: absolute;
      bottom: -30px;
    }
  }
  .ml10 {
    margin-left: 10px;
  }

  .mr10 {
    margin-right: 10px;
  }

  .red {
    color: red;
  }

  .v_a_b {
    vertical-align: bottom;
  }

}

.footer {
  display: flex;
  justify-content: space-between;
  border: 1px solid #ddd;
  height: 40px;
  align-items: center;
  border-top: 0;
  padding: 0px 4px;
  .pagination {
    display: flex;
    justify-content: flex-end;
    :global(.next-pagination-pages) {
      margin-left: 8px;
    }
  }
}

`],sourceRoot:""}]),s.locals={content:"index__content__UsbFa",ml10:"index__ml10__gbbXx",mr10:"index__mr10__+Xfio",red:"index__red__fZRrX",v_a_b:"index__v_a_b__vt0w-",footer:"index__footer__d3Vuj",pagination:"index__pagination__eCSn0"};const c=s},53993:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__s9DPg .index__codeBox__q1aix {
    padding: 16px;
    background: #f2f4f5;
    color: #333;
    position: relative;
    padding-right: 30px;
  }
    .index__content__s9DPg .index__codeBox__q1aix p {
      margin-bottom: 0px
    }
    .index__content__s9DPg .index__codeBox__q1aix .index__codeCopy__s1e2p {
      cursor: pointer;
      width: 33px;
      height: 30px;
      background: #cfcfcf;
      position: absolute;
      top: 0;
      right: 0;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .index__content__s9DPg .index__codeBox__q1aix .index__copyIcon__Zjazw {
      color: #fff
    }`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingChaosAgent/K8s/index.css"],names:[],mappings:"AACE;IACE,aAAa;IACb,mBAAmB;IACnB,WAAW;IACX,kBAAkB;IAClB,mBAAmB;EAmBrB;IAlBE;MACE;IACF;IACA;MACE,eAAe;MACf,WAAW;MACX,YAAY;MACZ,mBAAmB;MACnB,kBAAkB;MAClB,MAAM;MACN,QAAQ;MACR,aAAa;MACb,uBAAuB;MACvB,mBAAmB;IACrB;IACA;MACE;IACF",sourcesContent:[`.content {
  .codeBox {
    padding: 16px;
    background: #f2f4f5;
    color: #333;
    position: relative;
    padding-right: 30px;
    p {
      margin-bottom: 0px
    }
    .codeCopy {
      cursor: pointer;
      width: 33px;
      height: 30px;
      background: #cfcfcf;
      position: absolute;
      top: 0;
      right: 0;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .copyIcon {
      color: #fff
    }
  }
}`],sourceRoot:""}]),s.locals={content:"index__content__s9DPg",codeBox:"index__codeBox__q1aix",codeCopy:"index__codeCopy__s1e2p",copyIcon:"index__copyIcon__Zjazw"};const c=s},87164:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__FbiwI {
  width: 100%;
  border: 1px solid #dfdfdf;
  padding: 15px;
}

.index__title__SXkRj {
  color: #000;
  font-size: 16px; 
  line-height: 32px;
}

.index__item__RsD6R {
  font-size: 12px;
  line-height: 22px;
  color: #333333;
}

.index__code__VnwOT {
  width: 100%;
  padding: 15px;
  background: #f9f9f9;
  border: 1px solid #e8e8e8;
}

.index__command__5KL86 {
  width: 70%;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingChaosAgent/Public/index.css"],names:[],mappings:"AAAA;EACE,WAAW;EACX,yBAAyB;EACzB,aAAa;AACf;;AAEA;EACE,WAAW;EACX,eAAe;EACf,iBAAiB;AACnB;;AAEA;EACE,eAAe;EACf,iBAAiB;EACjB,cAAc;AAChB;;AAEA;EACE,WAAW;EACX,aAAa;EACb,mBAAmB;EACnB,yBAAyB;AAC3B;;AAEA;EACE,UAAU;EACV,gBAAgB;EAChB,aAAa;EACb,8BAA8B;AAChC",sourcesContent:[`.content {
  width: 100%;
  border: 1px solid #dfdfdf;
  padding: 15px;
}

.title {
  color: #000;
  font-size: 16px; 
  line-height: 32px;
}

.item {
  font-size: 12px;
  line-height: 22px;
  color: #333333;
}

.code {
  width: 100%;
  padding: 15px;
  background: #f9f9f9;
  border: 1px solid #e8e8e8;
}

.command {
  width: 70%;
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
}`],sourceRoot:""}]),s.locals={content:"index__content__FbiwI",title:"index__title__SXkRj",item:"index__item__RsD6R",code:"index__code__VnwOT",command:"index__command__5KL86"};const c=s},1413:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__content__NIISX h1 {
    margin-bottom: 8px;
    line-height: normal;
  }
  .index__content__NIISX .index__container__621Df ul {
      display: flex;
    }
  .index__content__NIISX .index__container__621Df ul li {
        margin: 0 10px 10px 0;
        border: 1px solid #dedede;
        transition: all 0.5s;
      }
  .index__content__NIISX .index__container__621Df ul li:hover {
          transform: scale(1.01);
          border: 1px solid rgba(0, 112, 204, 0.5);
          box-shadow: 0 1px 8px 0 rgba(0, 112, 204, 0.36);
        }
  .index__content__NIISX .index__container__621Df ul li .index__install-card__46teB, .index__content__NIISX .index__container__621Df ul li .index__card-disable__y5K5a {
          width: 280px;
          height: 176px;
          background: #ffffff;
          box-shadow: 0 0 3px 0 #e0e0e0;
          cursor: pointer;
        }
  .index__content__NIISX .index__container__621Df ul li .index__install-card__46teB .index__install-card-text__uTaNG, .index__content__NIISX .index__container__621Df ul li .index__card-disable__y5K5a .index__install-card-text__uTaNG {
            padding-left: 9px;
            font-family: PingFangSC-Medium;
            font-size: 18px;
            letter-spacing: 0;
          }
  .index__content__NIISX .index__container__621Df ul li .index__install-card__46teB .index__install-card-text__uTaNG .index__txc__O1Pt3, .index__content__NIISX .index__container__621Df ul li .index__card-disable__y5K5a .index__install-card-text__uTaNG .index__txc__O1Pt3 {
              text-align: center;
            }
  .index__content__NIISX .index__container__621Df ul li .index__install-card__46teB .index__install-card-text__uTaNG .index__txc__O1Pt3 .index__i-ecs__HOheq, .index__content__NIISX .index__container__621Df ul li .index__card-disable__y5K5a .index__install-card-text__uTaNG .index__txc__O1Pt3 .index__i-ecs__HOheq {
                display: inline-block;
                width: 64px;
                height: 64px;
                background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFUElEQVR4Xu2aXWwUVRTH/2emrYAf6IMNH7Y7axNTIqbGGAW6nYoIhgBNa9ju1IcGNDHy2GdN6IPGEBLjg1Ff1BoSu9ONIuKDIlJ32FIT8SPYROsHM1sVqjVGjbWVdueYWdFs6X7MzN4lu9mdx517/uec35w75967Q6jyi6o8f9QA1CqgygnUpkCVF0DtJVibArUpUOUEalPgahVAOG4FJaLdYN4JQgDAGjBfS4RpMKYAfMaE8YbVK48faVsze7XiKnkF9CWSt9sp+xki2uMmKWb8RhKeTy2uOhTb2vinG5tixpQOADNFjORhIgwAkLwGyeBJyPIuvb35O6+2XsaXBMC+UXPFnEwxAnZ7CWb5WP6DUNc1rDbFi9PJbS0cwCCz9JVhvQui7WKC5gUb1DeiKm+I0VuqIhxAxLBeIOCA0GAZ8ym5/s5YaP2kUF1A7FK4N2FtlWycEh2ko8eMiQ1qoG2QyBapL7QCInHrCyJsFBngEi2i/mhH4IhIfWEAeg2zWwIdFRncMi3mb1pVpVVkFQgDoBnWOwB2lRSAM2cl2jEcCrwvyo8QAGHj4s0y/p720+89J8J4JdqpPOrZLoeBEACakdwLcExUUPl0GPiLwB+DKQXwLwCmWaJJW5aPxbY0/eg1BiEAInHrCSI85dW56PEMfOrEEe1QXL+LBAEwXyOiftEJFaF3hmWp380y2hOA8OjP18nS/C4mO0xAK4B1AG4qItCSmTLwK0nSg9FQ89l8TlwBSC9vT1uPATgE0A0li1qwMANzkKV79PbmiVzSBQH0jE03NizOHyPCJjfxMeNpkvikm7Gexth0GIS7Pdk4K0jw1w2rV96V64whL4DI2NRGStknAKx161gianm9I3De7fhc4yKG+RaYAtTAPdHNQUuLm4MgOuhPl16KqoGs+5OcALoSM9evSs2eA0Hx5LSeg07AnmyuGKyNmwoWyPz3Z94fVYND2unkQTAP+tFlRsqWG26NhdY5J09LrpwAtLipg6jXs0MBAByf6QoAFKpHd7oCigCQxgi8qqvKI64ARBLmJrJp3HPyAGyirpGOwHE/tvlsNMNyFlp7/eoy+JJdV3djbEvTXKZG1gqIxM33iGiHX2flasdEvXpHYMmKdRmAcOJCs2xfSpZrEsXElW0aLAOgGeYAQM/6dVRubTAzDwZO6qqy5KhuGYCIYR0loNsvgPJsg5f7CWNC71TuyPsO0AzzI4Du9QsAArqA6DaYkcuFqKqszw8gbpmee3+mogAApWiD6VbopgIices8EYJ+K6Bc2+DltcAHuqo8UKgCzoCw2S+A8rajoaga2J8XQMSwhgnQyjsRn9FlOVXO1gYfB+hFny6ceVZWu8HMPFK4pjGmrp3JXwFjUy2Usr/1C0BUG9QM80NnN5iq455Ye/Dz4naD6c3AqWinsu3KvLIuhdPOQZ2+IAjoApltkMEDuhp8rtjNkC3h/pGQMuoKQF8iuZ1tds4BvF8CADhONcMccnaDqMe+YneDDIzpqhLKlkzO7XAkbr7t9qOGTOFya4MMnlkAtb2pKhc9AXAOQCV59hMC3ea9DMrDIr0FlqRtsVAgkSuivEdiD43+cEuDvJAAyPmmp6IuZv7dZtoZu0/Je65R8FDUgVAvLZ4gwoaKIcA4y0C/3ql8WSjmggAcgTCzLCemHma2nyznKZH+rggY1NVgtFDi/913BeB/MWbSjO/3gOwDYG4BqAmEFW6dCR5nO/8NMuMnEJ1jCS9na3OFfHoDUEitAu/XAFTgQxMacq0ChOKsQLFaBVTgQxMacq0ChOKsQLFaBVTgQxMactVXwD9qjf1Q3MQj4AAAAABJRU5ErkJggg==)
                  no-repeat;
                position: relative;
                top: 15px;
              }
  .index__content__NIISX .index__container__621Df ul li .index__card-disable__y5K5a {
          background: #f1f1f1;
          cursor: not-allowed;
          box-shadow: none !important;
          border: none !important;
        }`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/SettingSelectEnv/index.css"],names:[],mappings:"AACE;IACE,kBAAkB;IAClB,mBAAmB;EACrB;EAEE;MACE,aAAa;IA0Cf;EAzCE;QACE,qBAAqB;QACrB,yBAAyB;QACzB,oBAAoB;MAqCtB;EApCE;UACE,sBAAsB;UACtB,wCAAwC;UACxC,+CAA+C;QACjD;EACA;UACE,YAAY;UACZ,aAAa;UACb,mBAAmB;UACnB,6BAA6B;UAC7B,eAAe;QAmBjB;EAlBE;YACE,iBAAiB;YACjB,8BAA8B;YAC9B,eAAe;YACf,iBAAiB;UAanB;EAZE;cACE,kBAAkB;YAUpB;EATE;gBACE,qBAAqB;gBACrB,WAAW;gBACX,YAAY;gBACZ;2BACW;gBACX,kBAAkB;gBAClB,SAAS;cACX;EAIN;UACE,mBAAmB;UACnB,mBAAmB;UACnB,2BAA2B;UAC3B,uBAAuB;QACzB",sourcesContent:[`.content {
  h1 {
    margin-bottom: 8px;
    line-height: normal;
  }
  .container {
    ul {
      display: flex;
      li {
        margin: 0 10px 10px 0;
        border: 1px solid #dedede;
        transition: all 0.5s;
        &:hover {
          transform: scale(1.01);
          border: 1px solid rgba(0, 112, 204, 0.5);
          box-shadow: 0 1px 8px 0 rgba(0, 112, 204, 0.36);
        }
        .install-card, .card-disable {
          width: 280px;
          height: 176px;
          background: #ffffff;
          box-shadow: 0 0 3px 0 #e0e0e0;
          cursor: pointer;
          .install-card-text {
            padding-left: 9px;
            font-family: PingFangSC-Medium;
            font-size: 18px;
            letter-spacing: 0;
            .txc {
              text-align: center;
              .i-ecs {
                display: inline-block;
                width: 64px;
                height: 64px;
                background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAFUElEQVR4Xu2aXWwUVRTH/2emrYAf6IMNH7Y7axNTIqbGGAW6nYoIhgBNa9ju1IcGNDHy2GdN6IPGEBLjg1Ff1BoSu9ONIuKDIlJ32FIT8SPYROsHM1sVqjVGjbWVdueYWdFs6X7MzN4lu9mdx517/uec35w75967Q6jyi6o8f9QA1CqgygnUpkCVF0DtJVibArUpUOUEalPgahVAOG4FJaLdYN4JQgDAGjBfS4RpMKYAfMaE8YbVK48faVsze7XiKnkF9CWSt9sp+xki2uMmKWb8RhKeTy2uOhTb2vinG5tixpQOADNFjORhIgwAkLwGyeBJyPIuvb35O6+2XsaXBMC+UXPFnEwxAnZ7CWb5WP6DUNc1rDbFi9PJbS0cwCCz9JVhvQui7WKC5gUb1DeiKm+I0VuqIhxAxLBeIOCA0GAZ8ym5/s5YaP2kUF1A7FK4N2FtlWycEh2ko8eMiQ1qoG2QyBapL7QCInHrCyJsFBngEi2i/mhH4IhIfWEAeg2zWwIdFRncMi3mb1pVpVVkFQgDoBnWOwB2lRSAM2cl2jEcCrwvyo8QAGHj4s0y/p720+89J8J4JdqpPOrZLoeBEACakdwLcExUUPl0GPiLwB+DKQXwLwCmWaJJW5aPxbY0/eg1BiEAInHrCSI85dW56PEMfOrEEe1QXL+LBAEwXyOiftEJFaF3hmWp380y2hOA8OjP18nS/C4mO0xAK4B1AG4qItCSmTLwK0nSg9FQ89l8TlwBSC9vT1uPATgE0A0li1qwMANzkKV79PbmiVzSBQH0jE03NizOHyPCJjfxMeNpkvikm7Gexth0GIS7Pdk4K0jw1w2rV96V64whL4DI2NRGStknAKx161gianm9I3De7fhc4yKG+RaYAtTAPdHNQUuLm4MgOuhPl16KqoGs+5OcALoSM9evSs2eA0Hx5LSeg07AnmyuGKyNmwoWyPz3Z94fVYND2unkQTAP+tFlRsqWG26NhdY5J09LrpwAtLipg6jXs0MBAByf6QoAFKpHd7oCigCQxgi8qqvKI64ARBLmJrJp3HPyAGyirpGOwHE/tvlsNMNyFlp7/eoy+JJdV3djbEvTXKZG1gqIxM33iGiHX2flasdEvXpHYMmKdRmAcOJCs2xfSpZrEsXElW0aLAOgGeYAQM/6dVRubTAzDwZO6qqy5KhuGYCIYR0loNsvgPJsg5f7CWNC71TuyPsO0AzzI4Du9QsAArqA6DaYkcuFqKqszw8gbpmee3+mogAApWiD6VbopgIices8EYJ+K6Bc2+DltcAHuqo8UKgCzoCw2S+A8rajoaga2J8XQMSwhgnQyjsRn9FlOVXO1gYfB+hFny6ceVZWu8HMPFK4pjGmrp3JXwFjUy2Usr/1C0BUG9QM80NnN5iq455Ye/Dz4naD6c3AqWinsu3KvLIuhdPOQZ2+IAjoApltkMEDuhp8rtjNkC3h/pGQMuoKQF8iuZ1tds4BvF8CADhONcMccnaDqMe+YneDDIzpqhLKlkzO7XAkbr7t9qOGTOFya4MMnlkAtb2pKhc9AXAOQCV59hMC3ea9DMrDIr0FlqRtsVAgkSuivEdiD43+cEuDvJAAyPmmp6IuZv7dZtoZu0/Je65R8FDUgVAvLZ4gwoaKIcA4y0C/3ql8WSjmggAcgTCzLCemHma2nyznKZH+rggY1NVgtFDi/913BeB/MWbSjO/3gOwDYG4BqAmEFW6dCR5nO/8NMuMnEJ1jCS9na3OFfHoDUEitAu/XAFTgQxMacq0ChOKsQLFaBVTgQxMacq0ChOKsQLFaBVTgQxMactVXwD9qjf1Q3MQj4AAAAABJRU5ErkJggg==)
                  no-repeat;
                position: relative;
                top: 15px;
              }
            }
          }
        }
        .card-disable {
          background: #f1f1f1;
          cursor: not-allowed;
          box-shadow: none !important;
          border: none !important;
        }
      }
    }
  }
}`],sourceRoot:""}]),s.locals={content:"index__content__NIISX",container:"index__container__621Df","install-card":"index__install-card__46teB","card-disable":"index__card-disable__y5K5a","install-card-text":"index__install-card-text__uTaNG",txc:"index__txc__O1Pt3","i-ecs":"index__i-ecs__HOheq"};const c=s},11037:(x,f,e)=>{"use strict";e.d(f,{Z:()=>c});var u=e(60994),y=e.n(u),o=e(93476),A=e.n(o),s=A()(y());s.push([x.id,`.index__headTop__FtRyc {
  font-size: 28px;
  display: inline-block;
  line-height: 40px;
  margin-bottom: 16px;
  color: #111111 100%;
  cursor: pointer;
}
  .index__headTop__FtRyc .next-icon:before {
    vertical-align: bottom;
    color: #111111;
    width: 24px;
    font-size: 24px;
  }
  .index__headTop__FtRyc .index__protectionAccess__BICmv {
    margin-left: 16px;
  }
`,"",{version:3,sources:["webpack://./pages/Manage/AgentSetting/Step/InstallAgent/index.css"],names:[],mappings:"AAAA;EACE,eAAe;EACf,qBAAqB;EACrB,iBAAiB;EACjB,mBAAmB;EACnB,mBAAmB;EACnB,eAAe;AAUjB;EATE;IACE,sBAAsB;IACtB,cAAc;IACd,WAAW;IACX,eAAe;EACjB;EACA;IACE,iBAAiB;EACnB",sourcesContent:[`.headTop {
  font-size: 28px;
  display: inline-block;
  line-height: 40px;
  margin-bottom: 16px;
  color: #111111 100%;
  cursor: pointer;
  :global(.next-icon:before) {
    vertical-align: bottom;
    color: #111111;
    width: 24px;
    font-size: 24px;
  }
  .protectionAccess {
    margin-left: 16px;
  }
}
`],sourceRoot:""}]),s.locals={headTop:"index__headTop__FtRyc",protectionAccess:"index__protectionAccess__BICmv"};const c=s},74248:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(22888),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},50064:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(74560),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},43858:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(95674),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},22e3:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(53993),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},8541:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(87164),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},88460:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(1413),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}},4345:(x,f,e)=>{"use strict";e.r(f),e.d(f,{default:()=>c});var u=e(1892),y=e.n(u),o=e(11037),A={};A.insert="head",A.singleton=!1;var s=y()(o.Z,A);const c=o.Z.locals||{}}}]);

//# sourceMappingURL=311.bundle.js.map