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
                <Predict :id="id" />
            </div>
        </div>
    </div>
</template>

<script>

    import Training from './trainingPanel.vue'
    import Predict from './predictPanel.vue'

    export default {
        name: 'ML',
        data() {
            return {
                tab: 1,
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
            Predict,
        },
    }
</script>