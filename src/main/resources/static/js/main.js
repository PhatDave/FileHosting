$(document).ready(function() {
	const pBarElement = $("#pBar");
	const pBarText = $("#pBarText");
	let fileInput = $("#file");
	const pBar = pBarElement.progressbar({
		value: 0,
		max: 100
	});
	const form = $("#target");

	let progressVector = Array();
	let timerInterval = 200;
	let progressMaxSize = 25;

	function updateProgressBar(value) {
		pBarElement.progressbar("value", value);

	}

	function setProgressBarMax(max) {
		pBarElement.progressbar("option", "max", max);

	}

	$('#file').change(function() {
		uploadFile();

	});

	function prettyPrint(number) {
		let GB = 1024 * 1024 * 1024
		let MB = 1024 * 1024
		let KB = 1024
		if (number > GB) {
			return (number / GB).toFixed(2) + " GB";
		} else if (number > MB) {
			return (number / MB).toFixed(1) + " MB";
		} else if (number > KB) {
			return (number / KB).toFixed(0) + " KB";
		} else {
			return number + " B";
		}

	}

	class PerformanceTimestamp {
		timestamp;
		fileSize;

		constructor(timestamp, fileSize) {
			this.timestamp = timestamp;
			this.fileSize = fileSize;
		}
	}

	function captureCurrentTime(fileSize) {
		while (progressVector.length > progressMaxSize) {
			progressVector.shift();
		}
		progressVector.push(new PerformanceTimestamp(performance.now(), fileSize));

	}

	function calculateUploadSpeed() {
		if (progressVector.length == 0) {
			return "0 B/S";
		}
		let last = progressVector[progressVector.length - 1];
		let first = progressVector[0];

		let timeDiff = last.timestamp - first.timestamp;
		let fileSizeDiff = last.fileSize - first.fileSize;

		// timeDiff in ms
		let speedFactor = 1 / (timeDiff / 1000);
		fileSizeDiff *= speedFactor;
		return prettyPrint(fileSizeDiff) + "/s";
	}

	function tryRemoveLastBenchmark() {
		if (progressVector.length > 0) {
			progressVector.shift();
		}
	}

	setInterval(tryRemoveLastBenchmark, timerInterval);

	function uploadFile() {
		let file = fileInput[0].files[0]
		let data = new FormData(form[0]);

		let xhr = new XMLHttpRequest();
		xhr.open("POST", "/");
		xhr.upload.addEventListener("progress", ({loaded, total}) => {
			let fileLoaded = prettyPrint(loaded);
			let fileTotal = prettyPrint(total)
			let percentage = ((loaded / total) * 100).toFixed(1);

			captureCurrentTime(loaded);
			let uploadSpeed = calculateUploadSpeed();
			pBarText.text(`Uploading ${file.name} ${fileLoaded} / ${fileTotal} (${percentage}%) ${uploadSpeed}`);

			setProgressBarMax(total);
			updateProgressBar(loaded);
		});
		xhr.send(data);
	}
});
