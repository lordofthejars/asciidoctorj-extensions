Users should receive alerts depending on price of stocks

Narrative: 
In order to sell stocks at optimum price
As a user
I want to receive alerts when stock is above a price

GivenStories: org/asciidoctor/asciidoctorj/extension/jbehave/userstory/refreshable_stocks.story

Scenario: Alerts are switched on/off depending on price and a threshold. 

Given a stock of <symbol> and a <threshold>
When the stock is traded at <price>
Then the alert status should be <status>
 
Examples:     
|symbol|threshold|price|status|
|STK1|10.0|5.0|OFF|
|STK1|10.0|11.0|ON|