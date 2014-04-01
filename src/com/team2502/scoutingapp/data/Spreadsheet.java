package com.team2502.scoutingapp.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class Spreadsheet {
	
	private static final int MAX_ROWS = 100;
	private static final int MAX_COLS = 23;
	private static final String KEY = "";
	private SpreadsheetService service;
	
	public Spreadsheet(String spreadsheetName) {
		service = new SpreadsheetService(spreadsheetName);
	}
	
	public void connect(String username, String password) throws AuthenticationException {
		service.setUserCredentials(username, password);
	}
	
	public void update() throws IOException, ServiceException {
		FeedURLFactory urlFactory = FeedURLFactory.getDefault();
		URL cellFeedUrl = urlFactory.getCellFeedUrl(KEY, "od6", "private", "full");
		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		List<CellAddress> cellAddrs = new ArrayList<CellAddress>();
		for (int row = 1; row <= MAX_ROWS; ++row) {
			for (int col = 1; col <= MAX_COLS; ++col) {
				cellAddrs.add(new CellAddress(row, col));
			}
		}
		Map<String, CellEntry> cellEntries = getCellEntryMap(service, cellFeedUrl, cellAddrs);
		
	}
	
	public static Map<String, CellEntry> getCellEntryMap(SpreadsheetService ssSvc, URL cellFeedUrl, List<CellAddress> cellAddrs) throws IOException, ServiceException {
		CellFeed batchRequest = new CellFeed();
		for (CellAddress cellId : cellAddrs) {
			CellEntry batchEntry = new CellEntry(cellId.row, cellId.col, cellId.idString);
			batchEntry.setId(String.format("%s/%s", cellFeedUrl.toString(), cellId.idString));
			BatchUtils.setBatchId(batchEntry, cellId.idString);
			BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
			batchRequest.getEntries().add(batchEntry);
		}
		
		CellFeed cellFeed = ssSvc.getFeed(cellFeedUrl, CellFeed.class);
		CellFeed queryBatchResponse = ssSvc.batch(new URL(cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM).getHref()), batchRequest);
		
		Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>(cellAddrs.size());
		for (CellEntry entry : queryBatchResponse.getEntries()) {
			cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
			System.out.printf("batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n", BatchUtils.getBatchId(entry), entry.getId(), entry.getEditLink().getHref(), entry.getCell().getInputValue());
		}
		
		return cellEntryMap;
	}
	
}
