package co.edu.escuelaing
package schema

case class RecommendationTrend(period: String, strongBuy: Int, buy: Int, hold: Int, sell: Int, strongSell: Int, symbol: String)