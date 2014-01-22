package bitcoinrpc


import argonaut._, argonaut.Argonaut._
import scala.Some

/**
 * Created with IntelliJ IDEA.
 * User: jakoblind
 * Date: 2014-01-20
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */

case class JSONRPCRequest(id: String, method: String, params: Json)
object JSONRPCRequest {
  implicit def JSONRPCRequestJson =
    casecodec3(JSONRPCRequest.apply, JSONRPCRequest.unapply)("id", "method", "params")
}

case class JSONRPCResponse(result: Json, error: Option[String], id: String)
object JSONRPCResponse {
  implicit def JSONRPCResponseJson =
    casecodec3(JSONRPCResponse.apply, JSONRPCResponse.unapply)("result", "error", "id")
}

class JSONRPC(rpcURL: String, username: String, password: String) {
  import dispatch._, Defaults._

  def doRequest[A](req: JSONRPCRequest, responseTransformer: (Json => A)) = {
    println("DO REQUEST: " + req.asJson.toString())
    val dispatchReq = url(rpcURL).addHeader("Content-Type", "application/x-www-form-urlencoded").POST.as(username, password) << req.asJson.toString()
    val resp = Http(dispatchReq OK as.String).either.right.map(_.decodeOption[JSONRPCResponse])

    resp() match {
      case Right(Some(JSONRPCResponse(r, None, _))) => Right(responseTransformer(r))
      case Right(Some(JSONRPCResponse(_, Some(error), _))) => Left(error)
      case Right(_) => Left("No response case class found")
      case Left(t) => Left(t.getMessage)
    }
  }
}

object BitcoinRPC {
  val jsonToString:(Json => String) = (a => a.string.get.toString)

  val emptyjArray = jArray(List())

  def jStringInjArray(s: String) =  jArray(List(jString(s)))

  def getinfo(implicit jsonrpc: JSONRPC) = {
    jsonrpc.doRequest(JSONRPCRequest("t0", "getinfo", emptyjArray), identity[Json])
  }
  def getnewaddress(implicit jsonrpc: JSONRPC) = {
    jsonrpc.doRequest(JSONRPCRequest("t0", "getnewaddress", emptyjArray), jsonToString)
  }

  def validateaddress(address: String)(implicit jsonrpc: JSONRPC) = {
    jsonrpc.doRequest(JSONRPCRequest("t0", "validateaddress",jStringInjArray(address)), identity[Json])
  }

  def addmultisigaddress(addresses: List[String])(implicit jsonrpc: JSONRPC) = {
    val json = jArray(List(jNumber(addresses.size), jArray(addresses map (jString(_)))))
    jsonrpc.doRequest(JSONRPCRequest("t0", "addmultisigaddress", json), jsonToString)
  }

  def sendtoaddress(to: String, amount: Double)(implicit jsonrpc: JSONRPC) = {
    val json = jArray(List(jString(to), jNumber(amount)))
    jsonrpc.doRequest(JSONRPCRequest("t0", "sendtoaddress", json), jsonToString)
  }

  def getrawtransaction(tid: String, verbose: Boolean)(implicit jsonrpc: JSONRPC) = {
    val json = jArray(List(jString(tid), jNumber(if (verbose) 1 else 0)))
    jsonrpc.doRequest(JSONRPCRequest("t0", "getrawtransaction", json), identity[Json])
  }

  def createrawtransaction(txid: String, vout: Int, toAddr: String, amount: Double)(implicit jsonrpc: JSONRPC) = {
    val p1 = jArray(List(Json.obj("txid" -> jString(txid), "vout" -> jNumber(vout))))
    val json = jArray(List(p1, Json.obj(toAddr -> jNumber(amount))))
    jsonrpc.doRequest(JSONRPCRequest("t0", "createrawtransaction", json), jsonToString)
  }

  def signrawtransaction(t: String)(implicit jsonrpc: JSONRPC) = {
    val json = jStringInjArray(t)
    jsonrpc.doRequest(JSONRPCRequest("t0", "signrawtransaction", json), identity[Json])
  }
}

object JSONRPCMain extends App{
  val btcurl="http://127.0.0.1:18332"

  implicit val jsonrpc = new JSONRPC(btcurl, "jakob", "lind")

/*
  val resp = BitcoinRPC.getnewaddress.right.map(
    a => BitcoinRPC.validateaddress(a)
  )
*/
  //val resp = BitcoinRPC.addmultisigaddress(List("mmjTqkb2F7dRRwfzp12cTmgs7GQTaJTAkd", "n2AvboULvEJmATEmtWs6cYzRcELXQEwX1c"))
  //val resp = BitcoinRPC.sendtoaddress("2N3UMNL8hZ87kS1uRtMVzpRrY4yZyY3NqTa", 0.01)
  //val resp = BitcoinRPC.getrawtransaction("e6ae48d61c8914dbe23fdf88471073b24549d807b1e622810fb43e1e8139e379", true)
  val resp = BitcoinRPC.signrawtransaction("0100000001a531d73aa5c71f85e8abf44d2d397c9700eda673ab2c421a341ded6d7242d23101000000da00473044022040a4a2c3a7452d3fd61d989d7619c765744d702157d99ae9e0ad08e31b62c04a02205d066b8a24edc6de92d82d52622475fc88a2c275b62270f1af2b0a8231f588cd01483045022100d84475e0b96912481e0fa18ac829d6a286bd250074f3a8750a65b725d65ebfaf0220375aa0111dd10209e6aeba94a5288bbfc3dec1ac04cddfbc91464c5c3ca467b90147522102bc7b00d7b0a4ec4209e486417e1ef21138ce1910da5457d4e119d14f942a6a8a21033bfbb4a392b385c7271e38308d7a1f8dda33b667c0754c2c8e6e5c989f69769352aeffffffff02302d8900000000001976a914ec0b505e21d114210694154962de2937f8acc0e388ac40420f000000000017a914702cd3719394115f1ce4fa14c4c6eef79a418fc78700000000")
  println(resp)


}
