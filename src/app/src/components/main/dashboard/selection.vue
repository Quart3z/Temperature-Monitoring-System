<template>
    <div id="selection" class="card bg-white m-3">
        <div class="card-body p-2">
            <div class="d-flex justify-content-between">
                <!-- <div class="w-25">
                    <select class="form-select border-0 border-bottom" v-model="selectedFile" v-on:click="getDatasets" v-on:change="getDataset" aria-label="Default select example">
                        <option disabled value="">Select dataset</option>
                        <option v-for="dataset in datasets" :key="dataset" v-bind:value="dataset">{{ dataset }}</option>
                    </select>
                </div> -->
                <div class="navbar-nav">
                    <a class="nav-item dropdown" data-bs-toggle="modal" data-bs-target="#upload-modal">
                        <i class="bi-cloud-upload fs-4 px-2"></i>
                    </a>
                </div>
            </div>
        </div>
        <UploadModal :id=id />
    </div>
</template>

<script>
    import UploadModal from "../dataset/uploadModal.vue"

    export default {
        name: 'Selection',
        data() {
            return {
                // datasets: [],
                // selectedFile: ""
            }
        },
        props: {
            id: null
        },
        methods: {
            // getDatasets: function () {

            //     const request = {
            //         method: "POST",
            //         headers: {
            //             "Content-Type": "application/json"
            //         },
            //         body: JSON.stringify({
            //             id: this.id
            //         })
            //     };

            //     fetch("/getDatasets", request)
            //         .then(response => {
            //             if (response.status == 200) {
            //                 response.json().then(data => {
            //                     this.datasets = data;
            //                 })

            //             } else {
            //                 console.log(response)
            //             }
            //         })

            // },
            // getDataset: function () {

            //     const request = {
            //         method: "POST",
            //         headers: {
            //             "Content-Type": "application/json"
            //         },
            //         body: JSON.stringify({
            //             id: this.id,
            //             filename: this.selectedFile
            //         })
            //     };

            //     fetch("/getDataset", request)
            //         .then(response => {
            //             if (response.status == 200) {
            //                 response.json().then(data => {
            //                     this.datasets = data;
            //                 })

            //             } else {
            //                 console.log(response)
            //             }
            //         })

            // }
        },
        components: {
            UploadModal
        },
    }
</script>