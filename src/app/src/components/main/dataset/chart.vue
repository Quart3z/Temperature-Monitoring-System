<template>
    <div id="chart" class="card bg-white m-3 p-3">
        <div class="card-title">
            <button class="btn btn-sm btn-primary mb-3" type="button" data-bs-toggle="collapse" data-bs-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                Filter
            </button>
            <div class="collapse container" id="collapseExample">
                <div class="row mb-3">
                    <div class="col d-flex flex-column">
                        <label class="form-label">Timestamp</label>
                        <datepicker v-model="period" type="datetime" range :clearable="false" v-on:change="getData" />
                    </div>
                </div>
                <div class="row mb-3">
                    <div class="col">
                        <label class="form-label">No. of nodes</label>
                        <vue-slider v-model="count" v-on:change="getData" />
                    </div>
                    <div class="col">
                        <label class="form-label">Temperature</label>
                        <vue-slider v-model="temperature" v-on:change="getData" :enable-cross="false" :interval="0.01" />
                    </div>
                </div>
                <hr>
            </div>
        </div>
        <div class="card-body">
            <highcharts :options="chartConfig" :key="redraw" />
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

    // Date picker
    import DatePicker from 'vue2-datepicker';
    import 'vue2-datepicker/index.css';

    // Range slider
    import VueSlider from 'vue-slider-component'
    import 'vue-slider-component/theme/default.css'

    Exporting(highcharts)
    ExportData(highcharts)
    export default {
        name: 'Chart',
        data() {
            return {
                redraw: true,
                count: "5",
                minReading: 10,
                maxReading: 50,
                temperature: [10, 50],
                period: [0, Date.now()],
                datasets: [],
                chartConfig: {
                    time: {
                        useUTC: false
                    },
                    title: {
                        text: "Devices Temperature"
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

            getData: function () {

                if (this.period == [null, null]) {
                    this.period = [0, Date.now()]
                }

                let timestampRange = this.period.slice();

                timestampRange.forEach(function (part, index) {
                    this[index] = new Date(new Date(this[index]).toLocaleString()).getTime()
                }, timestampRange)

                const request = {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: this.id,
                        filter: {
                            count: this.count,
                            period: timestampRange,
                            temperature: this.temperature
                        }
                    })
                };

                fetch("/getData", request)
                    .then(response => {
                        if (response.status == 200) {
                            response.json().then(entries => {

                                this.datasets = []

                                for (let entry in entries) {
                                    this.datasets.push({

                                        name: "Device " + entry,
                                        data: entries[entry].map((e) => {
                                            return [e.timestamp, e.temperature]
                                        })

                                    })
                                }

                                this.chartConfig.series = this.datasets;
                                this.redraw = !this.redraw
                            })

                        } else {
                            console.log(response)
                        }
                    })

            },

        },
        mounted() {

            this.getData();

        },
        components: {
            highcharts: Chart,
            datepicker: DatePicker,
            VueSlider
        },
    }
</script>