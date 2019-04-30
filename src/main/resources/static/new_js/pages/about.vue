<template>
  <c-default-page></c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("new_js/components/c-default-page.vue")
  },
  data: function() {
    return {};
  }
};
</script>