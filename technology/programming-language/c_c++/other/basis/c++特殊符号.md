# c++ 特殊符号

**1级**[**优先级**](http://wenwen.soso.com/z/Search.e?sp=S优先级&ch=w.search.yjjlink&cid=w.search.yjjlink) 左结合

　　() 圆括号 

　　[] 下标[运算符](http://wenwen.soso.com/z/Search.e?sp=S运算符&ch=w.search.yjjlink&cid=w.search.yjjlink) 

　　-> 指向[结构体](http://wenwen.soso.com/z/Search.e?sp=S结构体&ch=w.search.yjjlink&cid=w.search.yjjlink)成员运算符 

　　. 结构体成员运算符 

　　**2级优先级** 右结合 

　　! [逻辑](http://wenwen.soso.com/z/Search.e?sp=S逻辑&ch=w.search.yjjlink&cid=w.search.yjjlink)非运算符 

　　~ 按位取反运算符 

　　++ 前缀增量运算符 

　　-- 前缀增量运算符 

　　+ 正号运算符

　　- 负号运算符 

　　(类型) [类型转换](http://wenwen.soso.com/z/Search.e?sp=S类型转换&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符 

　　* 指针运算符 

　　& 地址与运算符 

　　sizeof 长度运算符 

　　**3级优先级** 左结合 

　　* [乘法](http://wenwen.soso.com/z/Search.e?sp=S乘法&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符 

　　/ 除法运算符 

　　% 取余运算符 

　　**4级优先级** 左结合

　　+ 加法运算符 

　　- 减法运算符 

　　**5级优先级** 左结合 

　　<< 左移运算符 

　　>> 右移运算符 

　　**6级优先级** 左结合 

　　<、<=、>、>= [关系运算符](http://wenwen.soso.com/z/Search.e?sp=S关系运算符&ch=w.search.yjjlink&cid=w.search.yjjlink) 

　　**7级优先级** 左结合 

　　== 等于运算符 

　　!= 不等于运算符 

　　**8级优先级** 左结合 

　　& 按位与运算符 

　　**9级优先级** 左结合 

　　^ 按位[异或](http://wenwen.soso.com/z/Search.e?sp=S异或&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符 

　　**10级优先级** 左结合 

　　| 按位或运算符 

　　**11级优先级** 左结合 

　　&& [逻辑与](http://wenwen.soso.com/z/Search.e?sp=S逻辑与&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符 

　　**12级优先级** 左结合 

　　|| [逻辑或](http://wenwen.soso.com/z/Search.e?sp=S逻辑或&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符 

　　**13级优先级** 右结合 

　　? : [条件运算符](http://wenwen.soso.com/z/Search.e?sp=S条件运算符&ch=w.search.yjjlink&cid=w.search.yjjlink) 

　　**14级优先级** 右结合 

　　= += -= *= /= %= &= ^= |= <<= >>= 全为[赋值运算符](http://wenwen.soso.com/z/Search.e?sp=S赋值运算符&ch=w.search.yjjlink&cid=w.search.yjjlink)

　　**15级优先级** 左结合 

　　， 逗号运算符 

　　优先级从上到下依次递减，最上面具有最高的优先级，逗号[操作符](http://wenwen.soso.com/z/Search.e?sp=S操作符&ch=w.search.yjjlink&cid=w.search.yjjlink)具有最低的优先级。

　　所有的优先级中，只有三个优先级是从右至左结合的，它们是单目运算符、条件运算符、赋值运算符。其它的都是从左至右结合。

　　具有最高优先级的其实并不算是真正的运算符，它们算是一类特殊的操作。()是与函数相关，[]与[数组](http://wenwen.soso.com/z/Search.e?sp=S数组&ch=w.search.yjjlink&cid=w.search.yjjlink)相关，而－>及.是取结构成员。

　　其次是单目运算符，所有的单目运算符具有相同的优先级，因此在我认为的 真正的运算符中它们具有最高的优先级，又由于它们都是从右至左结合的，因此*p++与*(p++)等效是毫无疑问的。

　　另外在C语言里 没有前置后置之分 因为++ -- 是右结合所以 右侧优先运算 表现为 "后置优先级比较高" 的假象 前置和后置的区分是因为[运算符重载](http://wenwen.soso.com/z/Search.e?sp=S运算符重载&ch=w.search.yjjlink&cid=w.search.yjjlink)而后加入C++的

　　接下来是[算术](http://wenwen.soso.com/z/Search.e?sp=S算术&ch=w.search.yjjlink&cid=w.search.yjjlink)运算符，*、/、%的优先级当然比+、－高了。

　　[移位运算符](http://wenwen.soso.com/z/Search.e?sp=S移位运算符&ch=w.search.yjjlink&cid=w.search.yjjlink)紧随其后。

　　其次的关系运算符中，< <= > >=要比 == !=高一个级别，不大好理解。

　　所有的逻辑操作符都具有不同的优先级（单目运算符出外，！和~）

　　逻辑位操作符的"与"比"或"高，而"异或"则在它们之间。

　　跟在其后的&&比||高。

　　接下来的是条件运算符，赋值运算符及逗号运算符。

　　在C语言中，只有4个运算符规定了运算方向，它们是&&、| |、条件运算符及赋值运算符。

　　&&、| |都是先计算左边表达式的值，当左边表达式的值能确定整个表达式的值时，就不再计算右边表达式的值。如 a = 0 && b; &&运算符的左边位0，则右边表达式b就不再判断。

　　在条件运算符中。如a?b:c；先判断a的值，再根据a的值对b或c之中的一个进行求值。

　　赋值表达式则规定先对右边的表达式求值，因此使 a = b = c = 6;成为可能。