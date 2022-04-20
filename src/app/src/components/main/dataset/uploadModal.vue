<template>
    <div id="upload-modal" class="modal fade" data-bs-backdrop="static" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Upload Dataset</h5>
                    <button id="upload-close" type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="empty-region justify-content-center align-items-center" @dragover="dragOver" @dragleave="dragLeave" @drop="drop">
                        <div class="text-center text-secondary m-5 p-5">
                            <div v-if="!isUploading">
                                <div class="my-3 lh-lg" v-if="isDraggable">
                                    <b>Drag & Drop</b> a file<br>
                                    or
                                </div>
                                <div class="form-group">
                                    <input class="form-control form-control-sm" id="formFileSm" type="file" ref="uploader" name="dataset" @change="upload">
                                    <div v-bind:style="{display: uploadFail}" class="invalid-feedback">Fail to upload dataset</div>
                                </div>
                            </div>
                            <div class="d-flex flex-column align-items-center" v-else>
                                <div class="spinner-border text-primary mb-2" role="status"></div>
                                Uploading ...
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script>
    export default {
        name: 'Body',
        data() {
            return {
                isDraggable: false,
                isUploading: false,
                progress: 0,
                uploadFail: 'none'
            }
        },
        props: {
            id: null
        },
        methods: {

            dragOver(event) {
                event.preventDefault();
            },
            dragLeave(event) {
                event.currentTarget.classList.add("bg-gray")
            },
            drop(event) {
                event.preventDefault();
                this.$refs.uploader.files = event.dataTransfer.files;
                this.upload()
            },
            upload: function () {

                const file = this.$refs.uploader.files
                const data = new FormData()
                data.append('id', this.id)
                data.append('file', file[0])

                this.isUploading = true;

                const request = {
                    method: "POST",
                    body: data
                };

                fetch("/uploadDataset", request)
                    .then(response => {

                        for (let t = 0; t < 10; t++) {
                            this.progress += 10;
                        }

                        setTimeout(() => {

                            this.progress = 0;
                            this.isUploading = false;

                            if (response.status == 200) {
                                document.getElementById("upload-close").click();
                                this.uploadFail = 'none';
                            } else {
                                console.log(response);
                                this.uploadFail = 'block';
                            }

                        }, 1000);

                    })

            },
            delay: function (ms) {
                return function (x) {
                    return new Promise(resolve => setTimeout(() => resolve(x), ms))
                }
            }

        },
        mounted() {

            if ('draggable' in document.createElement('span')) {
                this.isDraggable = true;
            }

        },
        components: {

        },
    }
</script>