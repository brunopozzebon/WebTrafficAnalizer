window.onload = function (e) {
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(finishLoadingGooglePackages);
}

function finishLoadingGooglePackages() {
    let data = document.querySelector("#data").textContent;

    let splittedData = data.split(";");
    for (let index = 1; index < splittedData.length - 1; index++) {
        let line = splittedData[index];
        let firstSlashPosition = line.indexOf('_');
        let title = line.substring(0, firstSlashPosition);
        let data = line.substring(firstSlashPosition + 1, line.length);
        if (data != "") {
            let structuredData = transformDataToArrayObject(data);
            drawChart(title, structuredData, "space" + index);
        }
    }
}

function getWindowsSize(structuredData) {
    let windowsData = new Array();
    let windowData = new Array();
    windowData.push("Instant");
    windowData.push("WindowSize");
    windowsData.push(windowData);
    for (let index = 0; index < structuredData.length; index++) {
        const windowData = new Array();
        windowData.push(index + 1);
        windowData.push(parseInt(structuredData[index].windowSize));
        windowsData.push(windowData);
    }
    return windowsData;
}

function getLosses(structuredData) {
    let lossesData = new Array();
    let lossData = new Array();
    lossData.push("Instant");
    lossData.push("Losses");
    lossesData.push(lossData);
    for (let index = 0; index < structuredData.length; index++) {
        const lossData = new Array();
        lossData.push(index + 1);
        lossData.push(parseInt(structuredData[index].losses));
        lossesData.push(lossData);
    }
    return lossesData;
}

function getTimer(structuredData) {
    let timersData = new Array();
    let timerData = new Array();
    timerData.push("Instant");
    timerData.push("Temporizador");
    timersData.push(timerData);
    for (let index = 0; index < structuredData.length; index++) {
        const timerData = new Array();
        timerData.push(index + 1);
        timerData.push(parseInt(structuredData[index].timer));
        timersData.push(timerData);
    }
    return timersData;
}

function drawChart(title, structuredData, section) {
console.log(structuredData);
    let windowsSize = getWindowsSize(structuredData);
    let losses = getLosses(structuredData);
    let timers = getTimer(structuredData);

    var dataWindowSize = google.visualization.arrayToDataTable(windowsSize);
    var dataLossesSize = google.visualization.arrayToDataTable(losses);
    var dataTimerSize = google.visualization.arrayToDataTable(timers);

    var optionsLines = {
        title: title,
        legend: { position: 'bottom' }
    };

    var chartWindow = new google.visualization.LineChart(document.getElementById(section+"window"));
    var charLoss = new google.visualization.LineChart(document.getElementById(section+"losses"));
    var chartTime = new google.visualization.LineChart(document.getElementById(section+"timer"));


    chartWindow.draw(dataWindowSize, optionsLines);

    charLoss.draw(dataLossesSize, optionsLines);
    chartTime.draw(dataTimerSize, optionsLines);
}

function transformDataToArrayObject(data) {
    let splitedData = data.split(',');
    let vectorData = new Array();
    for (let index = 0; index < splitedData.length; index++) {
        let intantAnalysis = splitedData[index].split('_');
        let intantAnalysisObject = new Object();
        intantAnalysisObject.timer = intantAnalysis[0];
        intantAnalysisObject.windowSize = intantAnalysis[1];
        intantAnalysisObject.losses = intantAnalysis[2];
        vectorData.push(intantAnalysisObject);
    }
    return vectorData;
}