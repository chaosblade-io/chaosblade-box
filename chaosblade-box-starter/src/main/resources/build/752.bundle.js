(self.webpackChunk=self.webpackChunk||[]).push([[752],{79566:(G,B,e)=>{"use strict";Object.defineProperty(B,"__esModule",{value:!0});var m=e(30156);B.default=m.Checkbox},91714:function(G,B,e){"use strict";var m=this&&this.__assign||function(){return m=Object.assign||function(I){for(var A,F=1,D=arguments.length;F<D;F++){A=arguments[F];for(var h in A)Object.prototype.hasOwnProperty.call(A,h)&&(I[h]=A[h])}return I},m.apply(this,arguments)},M=this&&this.__importDefault||function(I){return I&&I.__esModule?I:{default:I}};Object.defineProperty(B,"__esModule",{value:!0});var P=M(e(27378)),T=e(30156),C=M(e(55839)),y=e(46949),O=function(I){return P.default.createElement(T.Step,m({stretch:!0},I))};C.default(O,T.Step),B.default=y.withThemeClass(O)},64287:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587),I=e(83452),A=e(95315),F=e(63774),D=e(92937);(function(h,d){if(!0)!(M=[B,e(15286),e(57379),e(81853),e(27378),e(84014)],m=d,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var U})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(h,d,U,v,l,S){"use strict";var i=e(67971);C(h,"__esModule",{value:!0}),h.default=void 0,d=i(d),U=i(U),v=i(v),l=g(l),S=i(S);function n(a){if(typeof y!="function")return null;var u=new y,f=new y;return(n=function(r){return r?f:u})(a)}function g(a,u){if(!u&&a&&a.__esModule)return a;if(a===null||T(a)!=="object"&&typeof a!="function")return{default:a};var f=n(u);if(f&&f.has(a))return f.get(a);var _={},r=C&&O;for(var s in a)if(s!=="default"&&Object.prototype.hasOwnProperty.call(a,s)){var t=r?O(a,s):null;t&&(t.get||t.set)?C(_,s,t):_[s]=a[s]}return _.default=a,f&&f.set(a,_),_}function Z(a,u){var f=I(a);if(A){var _=A(a);u&&(_=_.filter(function(r){return O(a,r).enumerable})),f.push.apply(f,_)}return f}function N(a){for(var u=1;u<arguments.length;u++){var f=arguments[u]!=null?arguments[u]:{};u%2?Z(Object(f),!0).forEach(function(_){(0,U.default)(a,_,f[_])}):F?D(a,F(f)):Z(Object(f)).forEach(function(_){C(a,_,O(f,_))})}return a}var j=function(u){var f=u.info,_=u.onChange,r=(0,l.useState)(f||{}),s=(0,v.default)(r,2),t=s[0],o=s[1];return(0,l.useEffect)(function(){f&&o(f)},[f]),(0,l.useEffect)(function(){_(t)},[t]),l.default.createElement("div",{className:S.default.content},l.default.createElement("div",{className:S.default.formItem},l.default.createElement("div",{className:S.default.label},"AK"),l.default.createElement(d.default,{style:{flexGrow:1},value:t.account,onChange:function(c){return o(N(N({},t),{},{account:c}))}})),l.default.createElement("div",{className:S.default.formItem},l.default.createElement("div",{className:S.default.label},"SK"),l.default.createElement(d.default,{style:{flexGrow:1},value:t.password,onChange:function(c){return o(N(N({},t),{},{password:c}))}})))},p=j;h.default=p})},4075:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587),I=e(83452),A=e(95315),F=e(63774),D=e(92937);(function(h,d){if(!0)!(M=[B,e(72153),e(15286),e(57379),e(17534),e(77809),e(81853),e(27378),e(84014),e(14870)],m=d,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var U})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(h,d,U,v,l,S,i,n,g,Z){"use strict";var N=e(67971);C(h,"__esModule",{value:!0}),h.default=void 0,d=N(d),U=N(U),v=N(v),l=N(l),S=N(S),i=N(i),n=p(n),g=N(g);function j(r){if(typeof y!="function")return null;var s=new y,t=new y;return(j=function(E){return E?t:s})(r)}function p(r,s){if(!s&&r&&r.__esModule)return r;if(r===null||T(r)!=="object"&&typeof r!="function")return{default:r};var t=j(s);if(t&&t.has(r))return t.get(r);var o={},E=C&&O;for(var c in r)if(c!=="default"&&Object.prototype.hasOwnProperty.call(r,c)){var w=E?O(r,c):null;w&&(w.get||w.set)?C(o,c,w):o[c]=r[c]}return o.default=r,t&&t.set(r,o),o}function a(r,s){var t=I(r);if(A){var o=A(r);s&&(o=o.filter(function(E){return O(r,E).enumerable})),t.push.apply(t,o)}return t}function u(r){for(var s=1;s<arguments.length;s++){var t=arguments[s]!=null?arguments[s]:{};s%2?a(Object(t),!0).forEach(function(o){(0,v.default)(r,o,t[o])}):F?D(r,F(t)):a(Object(t)).forEach(function(o){C(r,o,O(t,o))})}return r}var f=function(s){var t=s.info,o=s.onChange,E=(0,Z.useDispatch)(),c=(0,n.useState)(t||{}),w=(0,i.default)(c,2),R=w[0],x=w[1],Q=(0,n.useState)(null),L=(0,i.default)(Q,2),Y=L[0],W=L[1];(0,n.useEffect)(function(){t&&x(t)},[t]),(0,n.useEffect)(function(){o(R)},[R]);var J=function(){var b=(0,S.default)(regeneratorRuntime.mark(function K(){var z,V,H,q,k;return regeneratorRuntime.wrap(function(X){for(;;)switch(X.prev=X.next){case 0:if(z=R.account,V=R.password,H=R.host,q=R.port,z){X.next=3;break}return X.abrupt("return",l.default.error("\u8BF7\u8F93\u5165Mysql\u7528\u6237\u540D\uFF01"));case 3:if(V){X.next=5;break}return X.abrupt("return",l.default.error("\u8BF7\u8F93\u5165Mysql\u5BC6\u7801\uFF01"));case 5:if(H){X.next=7;break}return X.abrupt("return",l.default.error("\u8BF7\u8F93\u5165Mysql IP\u5730\u5740\uFF01"));case 7:if(q){X.next=9;break}return X.abrupt("return",l.default.error("\u8BF7\u8F93\u5165Mysql \u7AEF\u53E3\uFF01"));case 9:return X.next=11,E.migration.checkDbAccount(R);case 11:k=X.sent,k?(o(u(u({},R),{},{pass:!0})),W({pass:!0,msg:"\u9A8C\u8BC1\u901A\u8FC7\uFF01"})):(o(u(u({},R),{},{pass:!1})),W({pass:!1,msg:"\u9A8C\u8BC1\u5931\u8D25\uFF01"}));case 13:case"end":return X.stop()}},K)}));return function(){return b.apply(this,arguments)}}();return n.default.createElement("div",{className:g.default.content},n.default.createElement("div",{className:g.default.formItem},n.default.createElement("div",{className:g.default.label},"\u7528\u6237\u540D"),n.default.createElement(U.default,{style:{flexGrow:1},defaultValue:R.account,onChange:function(K){return x(u(u({},R),{},{account:K}))}})),n.default.createElement("div",{className:g.default.formItem},n.default.createElement("div",{className:g.default.label},"\u5BC6\u7801"),n.default.createElement(U.default,{style:{flexGrow:1},defaultValue:R.password,onChange:function(K){return x(u(u({},R),{},{password:K}))}})),n.default.createElement("div",{className:g.default.formItem},n.default.createElement("div",{className:g.default.label},"IP\u5730\u5740"),n.default.createElement(U.default,{style:{flexGrow:1},defaultValue:R.host,onChange:function(K){return x(u(u({},R),{},{host:K}))}})),n.default.createElement("div",{className:g.default.formItem},n.default.createElement("div",{className:g.default.label},"\u7AEF\u53E3"),n.default.createElement(U.default,{style:{flexGrow:1},defaultValue:R.port,onChange:function(K){return x(u(u({},R),{},{port:K}))}})),n.default.createElement("div",{style:{textAlign:"right",marginBottom:42}},Y&&n.default.createElement("span",{style:{color:Y.pass?"green":"red"}},Y.msg),n.default.createElement(d.default,{type:"primary",size:"small",onClick:function(){return J()}},"\u9A8C\u8BC1\u8FDE\u63A5")))},_=f;h.default=_})},56870:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587);(function(I,A){if(!0)!(M=[B,e(72153),e(17225),e(27378),e(84014)],m=A,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(I,A,F,D,h){"use strict";var d=e(67971);C(I,"__esModule",{value:!0}),I.default=void 0,A=d(A),F=d(F),D=v(D),h=d(h);function U(i){if(typeof y!="function")return null;var n=new y,g=new y;return(U=function(N){return N?g:n})(i)}function v(i,n){if(!n&&i&&i.__esModule)return i;if(i===null||T(i)!=="object"&&typeof i!="function")return{default:i};var g=U(n);if(g&&g.has(i))return g.get(i);var Z={},N=C&&O;for(var j in i)if(j!=="default"&&Object.prototype.hasOwnProperty.call(i,j)){var p=N?O(i,j):null;p&&(p.get||p.set)?C(Z,j,p):Z[j]=i[j]}return Z.default=i,g&&g.set(i,Z),Z}var l=function(){return D.default.createElement("div",{className:h.default.finish},D.default.createElement("div",{className:h.default.tips},D.default.createElement("div",{className:h.default.pHeader,style:{color:"green"}},D.default.createElement(F.default,{type:"check-circle"})," \u6570\u636E\u8FC1\u79FB\u6210\u529F"),D.default.createElement("div",{className:h.default.desp},"\u6570\u636E\u8FC1\u79FB\u6210\u529F\u540E\uFF0C\u53EF\u4EE5\u8DF3\u8F6C\u516C\u6709\u4E91\u67E5\u770B\uFF01")),D.default.createElement("div",{style:{marginTop:32,textAlign:"center"}},D.default.createElement("a",{href:"https://ahas.console.aliyun.com/index",target:"_blank"},D.default.createElement(A.default,{type:"primary"},"\u767B\u9646\u516C\u5171\u4E91"))))},S=(0,D.memo)(l);I.default=S})},50388:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587);(function(I,A){if(!0)!(M=[B,e(91714),e(72153),e(17225),e(17534),e(77809),e(81853),e(27378),e(84014),e(14870)],m=A,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(I,A,F,D,h,d,U,v,l,S){"use strict";var i=e(67971);C(I,"__esModule",{value:!0}),I.default=void 0,A=i(A),F=i(F),D=i(D),h=i(h),d=i(d),U=i(U),v=g(v),l=i(l);function n(p){if(typeof y!="function")return null;var a=new y,u=new y;return(n=function(_){return _?u:a})(p)}function g(p,a){if(!a&&p&&p.__esModule)return p;if(p===null||T(p)!=="object"&&typeof p!="function")return{default:p};var u=n(a);if(u&&u.has(p))return u.get(p);var f={},_=C&&O;for(var r in p)if(r!=="default"&&Object.prototype.hasOwnProperty.call(p,r)){var s=_?O(p,r):null;s&&(s.get||s.set)?C(f,r,s):f[r]=p[r]}return f.default=p,u&&u.set(p,f),f}var Z={READY:{name:"\u51C6\u5907\u4E2D",value:0,color:"grey",icon:"clock"},RUNNING:{name:"\u8FD0\u884C\u4E2D",value:1,color:"#0070cc",icon:"loading"},FAILED:{name:"\u5931\u8D25",value:2,color:"red",icon:"times-circle"},SUCCESS:{name:"\u6210\u529F",value:1,color:"green",icon:"check-circle"}},N=function(a){var u=a.onChangeStep,f=(0,S.useDispatch)(),_=(0,v.useState)(""),r=(0,U.default)(_,2),s=r[0],t=r[1],o=(0,v.useState)([]),E=(0,U.default)(o,2),c=E[0],w=E[1];(0,v.useEffect)(function(){R();var L=setInterval(function(){return R()},10*1e3);return function(){clearInterval(L)}},[]);var R=function(){var L=(0,d.default)(regeneratorRuntime.mark(function Y(){var W,J,b;return regeneratorRuntime.wrap(function(z){for(;;)switch(z.prev=z.next){case 0:return z.next=2,f.migration.queryMigrationResult({migration_flag:""});case 2:W=z.sent,W&&(J=W.progress,b=W.status,w(J),t(b),["SUCCESS"].includes(b)&&u(2));case 4:case"end":return z.stop()}},Y)}));return function(){return L.apply(this,arguments)}}(),x=function(){var L=(0,d.default)(regeneratorRuntime.mark(function Y(W){var J;return regeneratorRuntime.wrap(function(K){for(;;)switch(K.prev=K.next){case 0:return K.next=2,f.migration.startMigration({migration_flag:W});case 2:J=K.sent,J&&h.default.success("\u91CD\u65B0\u8FC1\u79FB\u4E2D\uFF01");case 4:case"end":return K.stop()}},Y)}));return function(W){return L.apply(this,arguments)}}(),Q=function(Y){return v.default.createElement("div",null,Y==null?void 0:Y.map(function(W,J){var b=W.name,K=W.status,z=W.type,V=Z[K]||{};return v.default.createElement("div",{className:l.default.proItem,key:J},v.default.createElement("div",null,b),v.default.createElement("div",{style:{color:V.color}},v.default.createElement(D.default,{type:V.icon,size:"xs"})," ",V.name,K==="FAILED"&&v.default.createElement(F.default,{type:"primary",text:!0,style:{marginLeft:8},size:"small",onClick:function(){return x(z)}},"\u91CD\u8BD5")))}))};return v.default.createElement("div",{className:l.default.progress},v.default.createElement("div",{className:l.default.tips},v.default.createElement("div",{className:l.default.pHeader},s==="FAILED"&&"\u6570\u636E\u8FC1\u79FB\u5931\u8D25"||"\u6570\u636E\u8FC1\u79FB\u4E2D"),v.default.createElement("div",{className:l.default.desp},"\u6570\u636E\u8FC1\u79FB\u8017\u65F6\u8F83\u957F\uFF0C\u8BF7\u8010\u5FC3\u7B49\u5F85\u3002"),v.default.createElement("div",{className:l.default.error},"\u8FC1\u79FB\u8FC7\u7A0B\u5982\u679C\u51FA\u73B0\u5931\u8D25\uFF0C\u53EF\u4EE5\u70B9\u91CD\u8BD5\uFF0C\u6216\u8054\u7CFB\u7CFB\u7EDF\u7BA1\u7406\u5458\uFF01"),v.default.createElement("div",{className:l.default.error},"\u63A2\u9488\u8FC1\u79FB\uFF0C\u53EA\u9488\u5BF9host\u63A2\u9488\uFF0Ck8s\u63A2\u9488\u8BF7\u624B\u52A8\u66FF\u6362\u6210\u516C\u6709\u4E91\u63A2\u9488\uFF0C\u4EE5\u9632\u6570\u636E\u8FC1\u79FB\u5931\u8D25\uFF01")),v.default.createElement(A.default,{direction:"ver",shape:"dot"},c==null?void 0:c.map(function(L,Y){console.log(L,Y);var W=L.name,J=L.items;return v.default.createElement(A.default.Item,{status:"process",key:Y,title:W,content:Q(J)})})))},j=(0,v.memo)(N);I.default=j})},96209:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587),I=e(83452),A=e(95315),F=e(63774),D=e(92937);(function(h,d){if(!0)!(M=[B,e(72153),e(79566),e(57379),e(17534),e(77809),e(81853),e(27378),e(84014),e(14870),e(64287),e(4075)],m=d,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var U})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(h,d,U,v,l,S,i,n,g,Z,N,j){"use strict";var p=e(67971);C(h,"__esModule",{value:!0}),h.default=void 0,d=p(d),U=p(U),v=p(v),l=p(l),S=p(S),i=p(i),n=u(n),g=p(g),N=p(N),j=p(j);function a(t){if(typeof y!="function")return null;var o=new y,E=new y;return(a=function(w){return w?E:o})(t)}function u(t,o){if(!o&&t&&t.__esModule)return t;if(t===null||T(t)!=="object"&&typeof t!="function")return{default:t};var E=a(o);if(E&&E.has(t))return E.get(t);var c={},w=C&&O;for(var R in t)if(R!=="default"&&Object.prototype.hasOwnProperty.call(t,R)){var x=w?O(t,R):null;x&&(x.get||x.set)?C(c,R,x):c[R]=t[R]}return c.default=t,E&&E.set(t,c),c}function f(t,o){var E=I(t);if(A){var c=A(t);o&&(c=c.filter(function(w){return O(t,w).enumerable})),E.push.apply(E,c)}return E}function _(t){for(var o=1;o<arguments.length;o++){var E=arguments[o]!=null?arguments[o]:{};o%2?f(Object(E),!0).forEach(function(c){(0,v.default)(t,c,E[c])}):F?D(t,F(E)):f(Object(E)).forEach(function(c){C(t,c,O(E,c))})}return t}var r=function(o){var E=o.onChangeStep,c=(0,Z.useDispatch)(),w=(0,n.useState)({cloudInfo:null,dbInfo:null,experiment_flag:!1,expertise_flag:!1}),R=(0,i.default)(w,2),x=R[0],Q=R[1],L=(0,n.useState)(!1),Y=(0,i.default)(L,2),W=Y[0],J=Y[1];(0,n.useEffect)(function(){b()},[]);var b=function(){var z=(0,S.default)(regeneratorRuntime.mark(function V(){var H,q;return regeneratorRuntime.wrap(function(ee){for(;;)switch(ee.prev=ee.next){case 0:return ee.next=2,c.migration.getMigrationConf();case 2:H=ee.sent,H?(q={cloudInfo:{account:H.cloudAk,password:H.cloudSk},dbInfo:{account:H.dbAccount,password:H.dbPassword,host:H.dbHost,port:H.dbPort},experiment_flag:H.experimentFlag,expertise_flag:H.expertiseFlag},Q(q)):l.default.error("\u83B7\u53D6\u914D\u7F6E\u4FE1\u606F\u5931\u8D25!");case 4:case"end":return ee.stop()}},V)}));return function(){return z.apply(this,arguments)}}(),K=function(){var z=(0,S.default)(regeneratorRuntime.mark(function V(){var H,q,k,ee,X,te,ne,ae;return regeneratorRuntime.wrap(function($){for(;;)switch($.prev=$.next){case 0:if(H=x||{},q=H.cloudInfo,k=H.dbInfo,ee=H.experiment_flag,X=H.expertise_flag,!(q===null)){$.next=3;break}return $.abrupt("return",l.default.error("\u8BF7\u586B\u5199\u516C\u6709\u4E91\u8D26\u53F7\uFF01"));case 3:if(q){$.next=5;break}return $.abrupt("return",l.default.error("\u516C\u6709\u4E91\u8D26\u53F7\u6821\u9A8C\u672A\u901A\u8FC7\uFF01"));case 5:if(!(!ee&&!X)){$.next=7;break}return $.abrupt("return",l.default.error("\u8BF7\u9009\u62E9\u8FC1\u79FB\u9009\u9879\uFF01"));case 7:if(!(!k===null)){$.next=9;break}return $.abrupt("return",l.default.error("\u8BF7\u586B\u5199Mysql\u914D\u7F6E\uFF01"));case 9:if(k.hasOwnProperty("pass")){$.next=11;break}return $.abrupt("return",l.default.error("\u8BF7\u586B\u5199Mysql\u5E76\u6821\u9A8C\u914D\u7F6E\uFF01"));case 11:if(k!=null&&k.pass){$.next=13;break}return $.abrupt("return",l.default.error("Mysql\u914D\u7F6E\u6821\u9A8C\u672A\u901A\u8FC7\uFF01"));case 13:return te={cloud_account:q.account,cloud_password:q.password,experiment_flag:ee,expertise_flag:X,db_account:k.account,db_password:k.password,db_host:k.host,db_port:k.port},J(!0),$.next=17,c.migration.saveMigrationConf(te);case 17:if(ne=$.sent,!ne){$.next=25;break}return $.next=21,c.migration.startMigration({migration_flag:""});case 21:ae=$.sent,ae?(l.default.success("\u914D\u7F6E\u6570\u636E\u4FDD\u5B58\u6210\u529F\uFF0C\u5F00\u59CB\u8FC1\u79FB\uFF01"),E(1),J(!1)):(l.default.error("\u8FC1\u79FB\u5931\u8D25\uFF01"),J(!1)),$.next=27;break;case 25:l.default.error("\u914D\u7F6E\u6570\u636E\u4FDD\u5B58\u5931\u8D25\uFF01"),J(!1);case 27:case"end":return $.stop()}},V)}));return function(){return z.apply(this,arguments)}}();return n.default.createElement("div",{className:g.default.setting},n.default.createElement("div",{className:g.default.header},"\u516C\u6709\u4E91\u8D26\u53F7AK/SK\u7ED1\u5B9A",n.default.createElement("span",{style:{fontSize:12,color:"#666",marginLeft:8,fontWeight:400}},"\u6CA1\u6709\u8D26\u53F7\uFF0C",n.default.createElement("a",{style:{color:"#0070cc"},href:"https://account.aliyun.com/register/register.htm",target:"_blank"},"\u7ACB\u5373\u6CE8\u518C\uFF01"))),n.default.createElement(N.default,{info:x.cloudInfo,onChange:function(V){return Q(_(_({},x),{},{cloudInfo:V}))}}),n.default.createElement("div",{className:g.default.header},"\u8FC1\u79FB\u9009\u9879"),n.default.createElement("div",{className:g.default.content},n.default.createElement("div",{className:g.default.formItem},n.default.createElement("div",{className:g.default.label},"\u8FC1\u79FB\u6F14\u7EC3\u6570\u636E"),n.default.createElement(U.default,{checked:x.experiment_flag,onChange:function(V){return Q(_(_({},x),{},{experiment_flag:V}))}}))),(x.experiment_flag||x.expertise_flag)&&n.default.createElement(n.default.Fragment,null,n.default.createElement("div",{className:g.default.header},"Mysql\u914D\u7F6E"),n.default.createElement(j.default,{info:x.dbInfo,onChange:function(V){return Q(_(_({},x),{},{dbInfo:V}))}})),n.default.createElement("div",{style:{textAlign:"center"}},n.default.createElement(d.default,{type:"primary",loading:W,onClick:function(){return K()}},"\u786E\u5B9A\u8FC1\u79FB\u6570\u636E")))},s=(0,n.memo)(r);h.default=s})},752:function(G,B,e){var m,M,P,T=e(24596),C=e(67394),y=e(93168),O=e(23587);(function(I,A){if(!0)!(M=[B,e(9863),e(91714),e(77809),e(81853),e(27378),e(96291),e(14870),e(56870),e(50388),e(96209)],m=A,P=typeof m=="function"?m.apply(B,M):m,P!==void 0&&(G.exports=P));else var F})(typeof globalThis!="undefined"?globalThis:typeof self!="undefined"?self:this,function(I,A,F,D,h,d,U,v,l,S,i){"use strict";var n=e(67971);C(I,"__esModule",{value:!0}),I.default=void 0,A=n(A),F=n(F),D=n(D),h=n(h),d=Z(d),l=n(l),S=n(S),i=n(i);function g(a){if(typeof y!="function")return null;var u=new y,f=new y;return(g=function(r){return r?f:u})(a)}function Z(a,u){if(!u&&a&&a.__esModule)return a;if(a===null||T(a)!=="object"&&typeof a!="function")return{default:a};var f=g(u);if(f&&f.has(a))return f.get(a);var _={},r=C&&O;for(var s in a)if(s!=="default"&&Object.prototype.hasOwnProperty.call(a,s)){var t=r?O(a,s):null;t&&(t.get||t.set)?C(_,s,t):_[s]=a[s]}return _.default=a,f&&f.set(a,_),_}var N=["\u8D26\u53F7\u914D\u7F6E","\u6570\u636E\u8FC1\u79FB","\u8FC1\u79FB\u7ED3\u679C"],j=function(){var u=(0,v.useDispatch)(),f=(0,d.useState)(!1),_=(0,h.default)(f,2),r=_[0],s=_[1],t=(0,d.useState)(0),o=(0,h.default)(t,2),E=o[0],c=o[1];(0,d.useEffect)(function(){u.pageHeader.setTitle("\u6570\u636E\u8FC1\u79FB"),u.pageHeader.setBreadCrumbItems(U.CHAOS_DEFAULT_BREADCRUMB_ITEM.concat([{key:"migration",value:"\u6570\u636E\u8FC1\u79FB",path:""}])),w()},[]);var w=function(){var x=(0,D.default)(regeneratorRuntime.mark(function Q(){var L;return regeneratorRuntime.wrap(function(W){for(;;)switch(W.prev=W.next){case 0:return s(!0),W.next=3,u.migration.queryMigrationResult({migration_flag:""});case 3:L=W.sent,(L==null?void 0:L.status)==="RUNNING"&&c(1),(L==null?void 0:L.status)==="FAILED"&&c(1),s(!1);case 7:case"end":return W.stop()}},Q)}));return function(){return x.apply(this,arguments)}}(),R=(0,d.useMemo)(function(){return E===2?d.default.createElement(l.default,null):E===1?d.default.createElement(S.default,{onChangeStep:function(Q){return c(Q)}}):d.default.createElement(i.default,{onChangeStep:function(Q){return c(Q)}})},[E]);return d.default.createElement(A.default,{visible:r,tip:"\u6570\u636E\u52A0\u8F7D\u4E2D\uFF0C\u8BF7\u7A0D\u540E\uFF01",style:{display:"block",minHeight:"420px"}},d.default.createElement(F.default,{current:E,shape:"circle"},N.map(function(x,Q){return d.default.createElement(F.default.Item,{key:Q,title:x})})),d.default.createElement("br",null),d.default.createElement("br",null),R)},p=(0,d.memo)(j);I.default=p})},5045:(G,B,e)=>{"use strict";e.d(B,{Z:()=>y});var m=e(60994),M=e.n(m),P=e(93476),T=e.n(P),C=T()(M());C.push([G.id,`.index__setting__h3o\\+o, .index__progress__jkye-, .index__finish__OCpoF {
  width: 790px;
  margin: 32px auto;
}

  .index__setting__h3o\\+o .index__header__MMIV-, .index__progress__jkye- .index__header__MMIV-, .index__finish__OCpoF .index__header__MMIV- {
    font-size: 14px;
    margin-bottom: 16px;
    font-weight: bold;
    background: #f1f1f1;
    padding: 8px 16px;
    border-left: 4px solid #f5f5f5;
  }

  .index__setting__h3o\\+o .index__content__ipaLF, .index__progress__jkye- .index__content__ipaLF, .index__finish__OCpoF .index__content__ipaLF {
    margin: 16px;
    margin-bottom: 32px;
  }

  .index__setting__h3o\\+o .index__formItem__yR35c, .index__progress__jkye- .index__formItem__yR35c, .index__finish__OCpoF .index__formItem__yR35c {
    margin-bottom: 16px;
    display: flex;
    align-items: center;
  }

  .index__setting__h3o\\+o .index__formItem__yR35c .index__label__aqhCL, .index__progress__jkye- .index__formItem__yR35c .index__label__aqhCL, .index__finish__OCpoF .index__formItem__yR35c .index__label__aqhCL {
      width: 90px;
      text-align: right;
      margin-right: 8px;
    }

  .index__setting__h3o\\+o .index__formItem__yR35c .index__label__aqhCL::after, .index__progress__jkye- .index__formItem__yR35c .index__label__aqhCL::after, .index__finish__OCpoF .index__formItem__yR35c .index__label__aqhCL::after {
        content: ': ';
      }

  .index__setting__h3o\\+o .index__tips__jBrsl, .index__progress__jkye- .index__tips__jBrsl, .index__finish__OCpoF .index__tips__jBrsl {
    background: #f5f5f5;
    padding: 16px;
    margin-bottom: 32px;
  }

  .index__setting__h3o\\+o .index__tips__jBrsl > div, .index__progress__jkye- .index__tips__jBrsl > div, .index__finish__OCpoF .index__tips__jBrsl > div {
      text-align: center;
    }

  .index__setting__h3o\\+o .index__tips__jBrsl .index__pHeader__pnYf7, .index__progress__jkye- .index__tips__jBrsl .index__pHeader__pnYf7, .index__finish__OCpoF .index__tips__jBrsl .index__pHeader__pnYf7 {
      font-size: 16px;
      font-weight: bold;
    }

  .index__setting__h3o\\+o .index__tips__jBrsl .index__desp__wGmgr, .index__progress__jkye- .index__tips__jBrsl .index__desp__wGmgr, .index__finish__OCpoF .index__tips__jBrsl .index__desp__wGmgr {
      color: #333;
      margin: 10px 0 6px;
    }

  .index__setting__h3o\\+o .index__tips__jBrsl .index__error__WCUTa, .index__progress__jkye- .index__tips__jBrsl .index__error__WCUTa, .index__finish__OCpoF .index__tips__jBrsl .index__error__WCUTa {
      color: red;
    }

  .index__setting__h3o\\+o .index__proItem__LQwvx, .index__progress__jkye- .index__proItem__LQwvx, .index__finish__OCpoF .index__proItem__LQwvx {
    display: flex;
    justify-content: space-between;
    padding-right: 32px;
    margin-bottom: 16px;
    color: #333;
  }
`,"",{version:3,sources:["webpack://./pages/Chaos/Migration/index.css"],names:[],mappings:"AAAA;EACE,YAAY;EACZ,iBAAiB;AAsDnB;;EApDE;IACE,eAAe;IACf,mBAAmB;IACnB,iBAAiB;IACjB,mBAAmB;IACnB,iBAAiB;IACjB,8BAA8B;EAChC;;EACA;IACE,YAAY;IACZ,mBAAmB;EACrB;;EACA;IACE,mBAAmB;IACnB,aAAa;IACb,mBAAmB;EAUrB;;EARE;MACE,WAAW;MACX,iBAAiB;MACjB,iBAAiB;IAInB;;EAHE;QACE,aAAa;MACf;;EAGJ;IACE,mBAAmB;IACnB,aAAa;IACb,mBAAmB;EAerB;;EAdE;MACE,kBAAkB;IACpB;;EACA;MACE,eAAe;MACf,iBAAiB;IACnB;;EACA;MACE,WAAW;MACX,kBAAkB;IACpB;;EACA;MACE,UAAU;IACZ;;EAEF;IACE,aAAa;IACb,8BAA8B;IAC9B,mBAAmB;IACnB,mBAAmB;IACnB,WAAW;EACb",sourcesContent:[`.setting, .progress, .finish {
  width: 790px;
  margin: 32px auto;

  .header {
    font-size: 14px;
    margin-bottom: 16px;
    font-weight: bold;
    background: #f1f1f1;
    padding: 8px 16px;
    border-left: 4px solid #f5f5f5;
  }
  .content {
    margin: 16px;
    margin-bottom: 32px;
  }
  .formItem {
    margin-bottom: 16px;
    display: flex;
    align-items: center;

    .label {
      width: 90px;
      text-align: right;
      margin-right: 8px;
      &::after {
        content: ': ';
      }
    }
  }
  .tips {
    background: #f5f5f5;
    padding: 16px;
    margin-bottom: 32px;
    > div {
      text-align: center;
    }
    .pHeader {
      font-size: 16px;
      font-weight: bold;
    }
    .desp {
      color: #333;
      margin: 10px 0 6px;
    }
    .error {
      color: red;
    }
  }
  .proItem {
    display: flex;
    justify-content: space-between;
    padding-right: 32px;
    margin-bottom: 16px;
    color: #333;
  }
}
`],sourceRoot:""}]),C.locals={setting:"index__setting__h3o+o",progress:"index__progress__jkye-",finish:"index__finish__OCpoF",header:"index__header__MMIV-",content:"index__content__ipaLF",formItem:"index__formItem__yR35c",label:"index__label__aqhCL",tips:"index__tips__jBrsl",pHeader:"index__pHeader__pnYf7",desp:"index__desp__wGmgr",error:"index__error__WCUTa",proItem:"index__proItem__LQwvx"};const y=C},84014:(G,B,e)=>{"use strict";e.r(B),e.d(B,{default:()=>y});var m=e(1892),M=e.n(m),P=e(5045),T={};T.insert="head",T.singleton=!1;var C=M()(P.Z,T);const y=P.Z.locals||{}}}]);

//# sourceMappingURL=752.bundle.js.map