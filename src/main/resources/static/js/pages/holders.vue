<template>
  <c-default-page :storage="storage">
      <b-button to="/holders/123">holder 123</b-button>
      <b-button to="/holders/new" variant="primary">new holder</b-button>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {};
  }
};
</script>
