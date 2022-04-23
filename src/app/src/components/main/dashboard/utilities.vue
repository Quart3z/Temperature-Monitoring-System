<template>
    <div id="selection" class="card bg-white m-3">
        <div class="card-body p-2">
            <div class="d-flex justify-content-between">
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
    import UploadModal from "./chart/uploadModal.vue"
    

    export default {
        name: 'Utilities',
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
        },
        components: {
            UploadModal
        },
    }
</script>