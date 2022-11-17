package co.edu.escuelaing
package schema

case class SymbolLookup(count: Int, result: List[SymbolLookupResult])

case class SymbolLookupResult(description: String, displaySymbol: String, symbol: String, `type`: String)
