<template>
  <div>
    <c-header/>
  </div>
</template>

<script>
module.exports = {
  components: {
    "c-header": httpVueLoader("new_js/components/c-header.vue")
  },
  data: function() {
    return {};
  }
};
</script>
