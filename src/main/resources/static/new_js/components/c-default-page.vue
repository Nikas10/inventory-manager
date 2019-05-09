<template>
  <div>
    <c-header :storage="storage"></c-header>
    <slot></slot>
    <c-footer></c-footer>
  </div>
</template>

<script>
module.exports = {
  components: {
    "c-header": httpVueLoader("new_js/components/c-header.vue"),
    "c-footer": httpVueLoader("new_js/components/c-footer.vue")
  },
  props: ["storage"]
};
</script>
