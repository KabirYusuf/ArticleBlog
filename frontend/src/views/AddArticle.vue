<template>
    <ProfileHeader>
        <div class="addArticleHeader__content">
            <div class="addArticleHeader__inner">
                <h2 class="article__title">Add New Article</h2>
                <router-link to="/my-profile" class="profile__link">Back to profile</router-link>
            </div>
        </div>
    </ProfileHeader>
    <section class="articleInfo__container">
        <h2 class="articleInfo__title">Add content</h2>
        <form @submit.prevent="handleSubmit">
            <div class="articleInfoContent__container">
                <div class="articleInfo_content">

                    <label for="title" class="form__label">Title</label>
                    <InputField id="" type="text" placeholder="set-title" v-model:value="title" class="input__field" />

                    <label for="slug" class="form__label">Slug</label>
                    <InputField id="slug" type="text" placeholder="set-slug" v-model:value="slug" class="input__field" />

                    <label for="content" class="form__label">Content</label>
                    <QuillEditor theme="snow" v-model:content="content" contentType="text"
                        style="height: 300px; width: 100%; background-color: white;color: #000;border: 1px solid #000;" />

                </div>
                <div class="articleInfo_content">

                    <label for="date" class="form__label">Date</label>
                    <InputField id="date" type="date" placeholder="dd/mm/yyyy" v-model:value="date" class="input__field" />

                    <div class="articleTagsAndPremium">
                        <h4 class="articleTagsAndPremium__title">Tags</h4>
                        <Button type="button" class="articleInfo__addTag">ADD TAG +</Button>
                        <div class="articlePremium__choice">
                            <InputField id="isPremium" type="checkbox" v-model:checked="isPremium"
                                class="checkbox__field" />
                            <label for="isPremium" class="form__label form__label--premium">Premium article</label>
                        </div>
                    </div>
                    <div class="articleInfo__uploadImage" @click="toggleUploader">Upload article image</div>
                    <ImageCropUpload ref="formData.image" :width="200" :height="200" :preview="true" lang-type="en"
                        v-model="showUploader" :url="uploadUrl" :params="params" :headers="headers" :img-format="imgFormat"
                        @crop-success="cropSuccess" @crop-upload-success="cropUploadSuccess"
                        @crop-upload-fail="cropUploadFail" />
                </div>

            </div>
            <div class="articleInfo__submitButtonContainer">
                <Button type="submit" class="articleInfo__submitButton">Add new</Button>
            </div>
        </form>
    </section>
    <Footer :isConnection="true" />
</template>

<script setup>
import ProfileHeader from '../components/layouts/ProfileHeader.vue';
import Footer from '../components/layouts/Footer.vue';
import InputField from '../components/inputs/InputField.vue';
import Button from '../components/inputs/Button.vue';
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import Swal from 'sweetalert2';
import { ref } from 'vue'
import { createArticle } from '../utility/articleApiService';
import { handleErrors } from '../utility/handleErrors';
import { useRouter } from 'vue-router';
import ImageCropUpload from 'vue-image-crop-upload';

const router = useRouter();

const title = ref('');
const slug = ref('');
const content = ref('');
const date = ref('');
const isPremium = ref(false);
const articleImageData = ref('');

const handleSubmit = async () => {

    if (!title.value.trim() || !content.value.trim()) {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Title and content are required!'
        });

        return
    }
    try {
        const response = await createArticle({
            title: title.value,
            slug: slug.value,
            content: content.value,
            date: date.value,
            isPremium: isPremium.value,
            articleImage: articleImageData.value
        })
        router.push({ name: 'my-profile' });

        Swal.fire({
            icon: 'success',
            title: 'Success',
            text: 'Article added successfully!'
        });

    } catch (error) {
        handleErrors(error)
    }
};

const showUploader = ref(false);
const imgDataUrl = ref('');
const uploadUrl = '/upload';
const imgFormat = 'png';
const params = ref({});
const headers = ref({});

const toggleUploader = () => {
    showUploader.value = !showUploader.value;
}

const cropSuccess = (imgDataUrlParam, field) => {
    imgDataUrl.value = imgDataUrlParam;

    const base64Image = imgDataUrlParam.replace(/^data:image\/[a-z]+;base64,/, '');

    articleImageData.value = base64Image;
    showUploader.value = false;
}

const cropUploadSuccess = (jsonData, field) => {
    console.log('Upload success:', jsonData);
}

const cropUploadFail = (status, field) => {
    console.error('Upload fail:', status);
}

</script>