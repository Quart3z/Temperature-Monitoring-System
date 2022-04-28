<template>
    <div id="machineLearning" class="card bg-white m-3 p-3">
        <ul class="nav nav-tabs" id="MLtabs" role="tablist">
            <li class="nav-item">
                <a v-bind:class="{'nav-link': true, 'active': tab === 1 }" v-on:click="tab = 1" aria-controls="train-panel" aria-selected="true">Train</a>
            </li>
            <li class="nav-item">
                <a v-bind:class="{'nav-link': true, 'active': tab === 2 }" v-on:click="tab = 2" aria-controls="predict-panel" aria-selected="false">Predict</a>
            </li>
        </ul>
        <div class="tab-content" id="myTabContent">
            <div v-bind:class="{'tab-pane fade': true, 'show active': tab === 1 }" role="tabpanel" aria-labelledby="train-panel">
                <Training :id="id"/>
            </div>
            <div v-bind:class="{'tab-pane fade': true, 'show active': tab === 2 }" role="tabpanel" aria-labelledby="predict-panel">
                <highcharts :options="chartConfig" :key="redraw" />
            </div>
        </div>
    </div>
</template>

<script>

    import Training from './trainingPanel.vue'

    // Highchart
    import {
        Chart
    } from 'highcharts-vue'
    import highcharts from 'highcharts'
    import Exporting from 'highcharts/modules/exporting'
    import ExportData from 'highcharts/modules/export-data'

    Exporting(highcharts)
    ExportData(highcharts)

    export default {
        name: 'ML',
        data() {
            return {
                tab: 1,
                redraw: true,
                datasets: [],
                chartConfig: {
                    time: {
                        useUTC: false
                    },
                    title: {
                        text: "Predicted Readings"
                    },
                    chart: {
                        spacingLeft: 30,
                        spacingRight: 30,
                    },
                    yAxis: {
                        type: 'float',
                        title: {
                            text: "Temperature",
                        }
                    },
                    xAxis: {
                        type: 'datetime',
                        title: {
                            text: "Timestamp"
                        }
                    },
                    legend: {
                        margin: 30,
                        align: 'right',
                        layout: "vertical",
                        verticalAlign: 'middle',
                        floating: false
                    },
                    exporting: {
                        enabled: true,
                        csv: {
                            decimalPoint: '.',
                            dateFormat: '%Y-%m-%d %H:%M:%S'
                        },
                        buttons: {
                            contextButton: {
                                menuItems: [
                                    'printChart',
                                    'downloadPNG',
                                    'downloadJPEG',
                                    'downloadPDF',
                                    'downloadSVG',
                                    'downloadCSV',
                                    'downloadJSON'
                                ]
                            }
                        }
                    },
                }
            }
        },
        props: {
            id: null
        },
        methods: {
        },
        mounted() {},
        components: {
            Training,
            highcharts: Chart,
        },
    }
</script>