![Screenshot](screen.png)

[Datum][1] is a simple [question answering][2] system. Natural language parsing and
generation is achieved through the use of templates. Fact retrieval and
inference is performed by a [Datalog][3] implementation, which uses the
"execution of concurrent machines" approach as described [here][4]. The fact and
rule databases are backed by JDO, so should be compatible with any database
system supported by a JDO implementation e.g. [DataNucleus][5].

Datum depends on the following libraries:
- [DataNucleus][5]
- [ANTLR v3][6]
- [Apache Commons Lang][7]

See the INSTALL file for compilation and usage information.

[1]: https://github.com/davidar/da.vidr.cc/blob/master/java/war/jsp/datum.jsp
[2]: http://en.wikipedia.org/wiki/Question_answering
[3]: http://en.wikipedia.org/wiki/Datalog
[4]: http://www.cs.sunysb.edu/~warren/xsbbook/node15.html
[5]: http://www.datanucleus.org/
[6]: http://antlr.org/
[7]: http://commons.apache.org/lang/
