<template>
    <div id="prediction" class="card bg-white m-3 p-3">
        <div class="card-title">
            <button class="btn btn-sm btn-primary mb-3" type="button" v-on:click="train" :disabled="isTrain || isPredict">
                Train
            </button>
            <button class="btn btn-sm btn-primary mb-3 ms-3" type="button" :disabled="isTrain || isPredict">
                Predict
            </button>
        </div>
        <div class="card-body">
            <div class="spinner-border" role="status" v-if="isTrain">
            </div>
            <!-- <div v-if="result.length > 0">
                <table>
                    <tr>
                        <th v-for="header in result[0]">{{ header }}</th>
                    </tr>
                    <tr>
                        <th v-for="result in result[1]">{{ result }}</th>
                    </tr>
                </table>
            </div> -->

            <highcharts :options="chartConfig" :key="redraw" v-if="isPredict" />
        </div>
    </div>
</template>

<script>
    // Highchart
    import {
        Chart
    } from 'highcharts-vue'
    import highcharts from 'highcharts'
    import Exporting from "highcharts/modules/exporting"
    import ExportData from 'highcharts/modules/export-data'


    Exporting(highcharts)
    ExportData(highcharts)

    export default {
        name: 'Chart',
        data() {
            return {
                redraw: true,
                isTrain: false,
                isPredict: false,
                datasets: [],
                result: [],
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

            train: function () {

                this.isTrain = true;

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                    })
                };

                fetch("/train", request)
                    .then(response => {
                        if (response.status == 200) {
                            console.log(response)
                            this.isTrain = false

                            response.text().then(text => {
                                let stats = text.split(/\s+/)
                                this.result = [stats.slice(0, 7), stats.slice(7, -1)]
                            })

                        } else {
                            console.log(response)
                        }
                    })

            },

        },
        mounted() {},
        components: {
            highcharts: Chart,
        },
    }
</script>