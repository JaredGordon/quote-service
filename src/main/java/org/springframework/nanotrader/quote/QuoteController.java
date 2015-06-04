package org.springframework.nanotrader.quote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quoteService")
public class QuoteController {

	@Autowired
	QuoteRepository quoteRepository;

	@RequestMapping("/count")
	public long countAllQuotes() {
		return quoteRepository.count();
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Quote findQuote(@PathVariable Integer id) {
		return quoteRepository.findOne(id);
	}

	@RequestMapping("/findAll")
	public Iterable<Quote> findAllQuotes() {
		return quoteRepository.findAll();
	}

	@RequestMapping("/findQuoteEntries")
	public List<Quote> findQuoteEntries(@RequestParam int firstResult,
			@RequestParam int maxResults) {
		return quoteRepository.findAll(
				new PageRequest(firstResult / maxResults, maxResults))
				.getContent();
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Quote saveQuote(@RequestBody Quote quote) {
		return quoteRepository.save(quote);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deleteQuote(@RequestBody Quote quote) {
		quoteRepository.delete(quote);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Quote updateQuote(@RequestBody Quote quote) {
		return quoteRepository.save(quote);
	}

	@RequestMapping("/findBySymbol/{symbol}")
	public Quote findBySymbol(@PathVariable String symbol) {
		return quoteRepository.findBySymbol(symbol);
	}

	@RequestMapping("/findBySymbolIn")
	public List<Quote> findBySymbolIn(@RequestParam Set<String> symbols) {
		return quoteRepository.findBySymbolIn(symbols);
	}

	@RequestMapping("/findAllPaged")
	public List<Quote> findAll(@RequestParam int page, @RequestParam int size) {
		return quoteRepository.findAll(new PageRequest(page, size))
				.getContent();
	}

	@RequestMapping("/indexAverage")
	public Long indexAverage() {
		return quoteRepository.findIndexAverage();
	}

	@RequestMapping("/openAverage")
	public Long openAverage() {
		return quoteRepository.findOpenAverage();
	}

	@RequestMapping("/volume")
	public Long volume() {
		return quoteRepository.findVolume();
	}

	@RequestMapping("/change")
	public Long change() {
		return quoteRepository.findChange();
	}

	@RequestMapping("/topGainers")
	public List<Quote> topGainers() {
		return quoteRepository
				.findAllByOrderByChange1Desc(new PageRequest(0, 3));
	}

	@RequestMapping("/topLosers")
	public List<Quote> topLosers() {
		return quoteRepository
				.findAllByOrderByChange1Asc(new PageRequest(0, 3));
	}

	@RequestMapping("/marketSummary")
	public Map<String, Long> marketSummary() {
		Map<String, Long> ms = new HashMap<String, Long>();
		ms.put("tradeStockIndexAverage", indexAverage());
		ms.put("tradeStockIndexOpenAverage", openAverage());
		ms.put("tradeStockIndexVolume", volume());
		ms.put("cnt", countAllQuotes());
		ms.put("change", change());

		return ms;
	}

//	@ExceptionHandler(Exception.class)
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public String handleException(Exception e) {
//		return e.getMessage();
//	}
	
//	@ExceptionHandler(IllegalArgumentException.class)
//	void handleBadRequests(HttpServletResponse response) throws IOException {
//	    response.sendError(HttpStatus.BAD_REQUEST.value(), "Please try again and with a non empty string as 'name'");
//	}
}
