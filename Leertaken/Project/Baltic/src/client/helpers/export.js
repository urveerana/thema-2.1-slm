var Export = {};

Export.make = function( stations, startDate, stopDate ) {
	if( ! stations || ! stations.length ) {
		console.error( 'Please select a station first.' );
		return;
	}
	if( ! ( startDate instanceof Date ) ) {
		startDate = new Date( startDate );
	}
	if( ! ( stopDate instanceof Date ) ) {
		stopDate = ( stopDate ? new Date( stopDate ) : new Date() );
	}

	window.location.href = '/export/' + stations.join( ',' ) + '/' + startDate.getTime() + '/' + stopDate.getTime();
};

Export.lastDay = function() {
	var prev = new Date();
	prev.setDate( prev.getDate() - 1 );
	this.make( app.Graph.getStations( true, true ), prev );
};

Export.lastWeek = function() {
	var prev = new Date();
	prev.setDate( prev.getDate() - 7 );
	this.make( app.Graph.getStations( true, true ), prev );
};

Export.lastMonth = function() {
	var prev = new Date();
	prev.setMonth( prev.getMonth() - 1 );
	this.make( app.Graph.getStations( true, true ), prev );
};

app.Export = Export;