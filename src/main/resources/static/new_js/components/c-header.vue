<template>
  <b-navbar class="c-header" toggleable="lg" type="light" variant="light">
    <b-navbar-brand to="start">Inventory Mananager</b-navbar-brand>

    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav>
        <b-nav-item v-for="page in pages" v-bind:key="page.id" v-bind:to="page.link">{{page.title}}</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto" v-if="storage.user">
        <b-nav-item-dropdown right>
          <template slot="button-content">
            <em>{{storage.user.login}}</em>
          </template>
          <b-dropdown-item to="profile">Profile</b-dropdown-item>
          <b-dropdown-item @click="onLogOut">Log Out</b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto" v-else>
        <b-nav-item to="signin">Sign In</b-nav-item>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
module.exports = {
  data: function() {
    return {
      storage: storage,
      pages: [
        {
          id: 0,
          title: "Requests",
          link: "requests"
        },
        {
          id: 1,
          title: "Equipment",
          link: "equipment"
        },
        {
          id: 2,
          title: "About",
          link: "about"
        }
      ]
    };
  },
  methods: {
    onLogOut() {
      this.$root.logout();

      // TODO Добавить перенаправление на стартовую страницу
    }
  }
};
</script>

<style>
.c-header {
  margin-bottom: 15pt;
}
</style>
