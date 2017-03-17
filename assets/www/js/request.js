var zoomLevel = 1;

$(function() {
	$.ajaxSetup({
		headers: {
			"X-CSRFToken": $("#csrf-token").val()
        },
        xhrFields: {
			withCredentials: true
        }
    });

	requestNews();
	requestHotNews();
	
	var date = new Date();
	var today = date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();
	$("#this-time").html("今天是：" + today);

	$("#subscribe-btn").click(function(e) {
		showLoadingBar();
		e.preventDefault = false;
		var emailAddr = $("#email-addr").val();
		addEmailAddr(emailAddr);
	});
	
	$("#unsubscribe-btn").click(function(e) {
		showLoadingBar();
		e.preventDefault = false;
		var emailAddr = $("#email-addr").val();
		delEmailAddr(emailAddr);
	});

	$("#zoom-in-btn").click(function(e) {
		e.preventDefault = false;
		updateZoom(0.5);
	});

	$("#zoom-out-btn").click(function(e) {
		e.preventDefault = false;
		updateZoom(-0.5);
	});
});

function updateZoom(zoom) {
	if((zoomLevel + zoom) > 1.5) {
		return;
	} else if((zoomLevel + zoom) === 0) {
		return;
	} else {
		zoomLevel += zoom;
	}

	$('#news-lists').css({ zoom: zoomLevel, '-moz-transform': 'scale(' + zoomLevel + ')' });
	$('#hot-news-lists').css({ zoom: zoomLevel, '-moz-transform': 'scale(' + zoomLevel + ')' });
}

function requestNews() {
	$.get("./anime/news", function(response) {
		var json = JSON.parse(JSON.stringify(response));
		if(json[0] === "empty") {
			$("#news-lists").append('<li>尚未有最新消息！</li>');
		} else {
			var renderStr = "";
			for(var index=0;index<json.length;index+=4) {
				renderStr += '<li><a target="_blank" href="' + json[index+2] + '">' +
					'<h2>' + json[index+3] + '(' + json[index] + ')' + '</h2>' +
					'<p>' + json[index+1] + '</p>' +
				'</a></li>';
			}
		}

		$("#news-lists").append(renderStr);
		$("#news-lists").listview("refresh");
	});
}

function requestHotNews() {
	$.get("./anime/hot/news", function(response) {
		var json = JSON.parse(JSON.stringify(response));
		if(json[0] === "empty") {
			$("#hot-news-lists").append('<li>尚未有今日新聞！</li>');
		} else {
			var renderStr = "";
			var checkLen = 1;
			for(var index=json.length-1;index>=0;index-=4) {
				renderStr += '<li><a target="_blank" href="' + json[index-1] + '">' +
					'<h2>' + json[index] + '(' + json[index-3] + ')' + '</h2>' +
					'<p>' + json[index-2] + '</p>' +
				'</a></li>';

				checkLen++;
				if(checkLen >= 6) {
					break;
				}
			}
		}

		$("#hot-news-lists").html(renderStr);
		$("#hot-news-lists").listview("refresh");
	});
}

function addEmailAddr(emailAddr) {
	$.post("./anime/subscribe", {"action": "subscribe", "email-addr": emailAddr}, function(response) {
		var json = JSON.parse(JSON.stringify(response));
		$("#error-msg").html(json["result"]);
		hideLoadingBar();
	});
}

function delEmailAddr(emailAddr) {
	$.post("./anime/unsubscribe", {"action": "unsubscribe", "email-addr": emailAddr}, function(response) {
		var json = JSON.parse(JSON.stringify(response));
		$("#error-msg").html(json["result"]);
		hideLoadingBar();
	});
}

function showLoadingBar() {
	$.mobile.loading("show");
}

function hideLoadingBar() {
	$.mobile.loading("hide");
}
