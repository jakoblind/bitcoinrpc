bitcoinrpc
==========

Scala library for doing JSON RPC commands to the official Bitcoin-QT application as described here https://en.bitcoin.it/wiki/Original_Bitcoin_client/API_Calls_list . Using dispatch for HTTP communication and argonaut for JSON parsing.

If you find this useful, consider a donation: Bitcoinaddress 1Az3tfNeagTUS24Lb2KAAQz5cHSYaVr7a3

Example on how to use it: 
```bash
$ sbt console
[warn] The global sbt directory is now versioned and is located at /Users/jakoblind/.sbt/0.13.
[warn]   You are seeing this warning because there is global configuration in /Users/jakoblind/.sbt but not in /Users/jakoblind/.sbt/0.13.
[warn]   The global sbt directory may be changed via the sbt.global.base system property.
[info] Loading project definition from /Users/jakoblind/dev/jakob/bitcoinrpc/project
[info] Set current project to bitcoinrpc (in build file:/Users/jakoblind/dev/jakob/bitcoinrpc/)
[info] Starting scala interpreter...
[info] 
Welcome to Scala version 2.10.3 (Java HotSpot(TM) 64-Bit Server VM, Java 1.6.0_65).
Type in expressions to have them evaluated.
Type :help for more information.

scala> import bitcoinrpc._
import bitcoinrpc._

scala> implicit val jsonrpc = new JSONRPC("http://127.0.0.1:18332", "jakob", "lind")
jsonrpc: bitcoinrpc.JSONRPC = bitcoinrpc.JSONRPC@1d1900d6

scala> import BitcoinRPC._
import BitcoinRPC._

scala> getnewaddress
res0: Product with Serializable with scala.util.Either[String,String] = Right(mzhnUUtEMc4ifokz7r3YY3B7uENnsJQGKb)

scala> validateaddress("mzhnUUtEMc4ifokz7r3YY3B7uENnsJQGKb")
res1: Product with Serializable with scala.util.Either[String,argonaut.Json] = Right({"isvalid":true,"pubkey":"0253f1e44c5d75c3bd4e78e3729f1c54f72c148b1d30ae99d5df667c269a69edf6","ismine":true,"isscript":false,"address":"mzhnUUtEMc4ifokz7r3YY3B7uENnsJQGKb","account":"","iscompressed":true})

scala> addmultisigaddress(List("mzhnUUtEMc4ifokz7r3YY3B7uENnsJQGKb"))
res2: Product with Serializable with scala.util.Either[String,String] = Right(2NCWv5zSdgmfx1pxAMDhAjY7YpLi1zNHDFn)
```

