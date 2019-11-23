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
        let title = transformTitle(line.substring(0, firstSlashPosition));
        
        let data = line.substring(firstSlashPosition + 1, line.length);
        document.getElementById("space"+index).textContent = title;
        if (data != "") {
            let structuredData = transformDataToArrayObject(data);
            drawChart(structuredData, "space" + index);
        }else{
            document.getElementById("space"+index+"window").innerHTML = "<p class='noData'>Nenhuma data cadastrada</p>";
        }
    }
}

function getWindowsSize(structuredData) {
    let windowsData = new Array();
    let windowData = new Array();
    windowData.push("Instante");
    windowData.push("WindowSize");
    windowsData.push(windowData);
    for (let index = 0; index < structuredData.length-1; index++) {
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
    lossData.push("Instante");
    lossData.push("Losses");
    lossesData.push(lossData);
    for (let index = 0; index < structuredData.length-1; index++) {
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
    timerData.push("Instante");
    timerData.push("Temporizador");
    timersData.push(timerData);
    for (let index = 0; index < structuredData.length-1; index++) {
        const timerData = new Array();
        timerData.push(index + 1);
        timerData.push(parseInt(structuredData[index].timer));
        timersData.push(timerData);
    }
    return timersData;
}

function getSequence(structuredData) {
    let sequencesData = new Array();
    let sequenceData = new Array();
    sequenceData.push("Instante");
    sequenceData.push("Sequencias");
    sequencesData.push(sequenceData);
    for (let index = 0; index < structuredData.length-1; index++) {
        const sequenceData = new Array();
        sequenceData.push(index + 1);
        sequenceData.push(parseInt(structuredData[index].sequence));
        sequencesData.push(sequenceData);
    }
    return sequencesData;
}

function drawChart(structuredData, section) {
    let windowsSize = getWindowsSize(structuredData);
    let losses = getLosses(structuredData);
    let timers = getTimer(structuredData);
    let sequences = getSequence(structuredData);

    let timeInSeconds = structuredData[structuredData.length-1] + " segundos de execução";

    var dataWindowSize = google.visualization.arrayToDataTable(windowsSize);
    var dataLossesSize = google.visualization.arrayToDataTable(losses);
    var dataTimerSize = google.visualization.arrayToDataTable(timers);
    var dataSequencesSize = google.visualization.arrayToDataTable(sequences);

    var optionsWindow = {
        legend: { position: 'bottom' },
        series: {
            0: { color: '#1c91c0' }
          },
          backgroundColor: { fill: "transparent" }
    };

    var optionsLosses = {
        legend: { position: 'bottom' },
        series: {
            0: { color: '#e2431e' }
          },
          backgroundColor: { fill: "transparent" }
    };

    var optionsTimer = {
        legend: { position: 'bottom' },
        series: {
            0: { color: '#6f9654' }
          },
          backgroundColor: { fill: "transparent" }
    };

    var optionsSequences = {
        legend: { position: 'bottom' },
        series: {
            0: { color: '#000000' }
          },
          backgroundColor: { fill: "transparent" }
    };

    var chartWindow = new google.visualization.LineChart(document.getElementById(section+"window"));
    var charLoss = new google.visualization.LineChart(document.getElementById(section+"losses"));
    var chartTime = new google.visualization.LineChart(document.getElementById(section+"timer"));
    var chartSequence = new google.visualization.LineChart(document.getElementById(section+"sequence"));

    document.getElementById(section+"seconds").textContent=timeInSeconds;
    chartWindow.draw(dataWindowSize, optionsWindow);

    charLoss.draw(dataLossesSize, optionsLosses);
    chartTime.draw(dataTimerSize, optionsTimer);
    chartSequence.draw(dataSequencesSize, optionsSequences);
}

function transformDataToArrayObject(data) {
    let splitedData = data.split(',');
    let vectorData = new Array();
    for (let index = 0; index < splitedData.length-1; index++) {
        let intantAnalysis = splitedData[index].split('_');
        let intantAnalysisObject = new Object();
        intantAnalysisObject.timer = intantAnalysis[0];
        intantAnalysisObject.windowSize = intantAnalysis[1];
        intantAnalysisObject.losses = intantAnalysis[2];
        intantAnalysisObject.sequence = intantAnalysis[3];
        vectorData.push(intantAnalysisObject);
    }
    vectorData.push(splitedData[splitedData.length-1]);
    return vectorData;
}

function transformTitle(title){
    switch(title){
        case "udpWithoutIperf":
            return "UDP sem Iperf";
            
        case "udpWithIperf":
                return "UDP com Iperf"
                
        case "tcpWithoutIperf":
                return "TCP sem Iperf"
                
        case "tcpWithIperf":
                return "TCP com Iperf"
    }
}