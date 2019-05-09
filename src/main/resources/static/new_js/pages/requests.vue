<template>
  <c-default-page :storage="storage"></c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("new_js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {};
  }
};
</script>