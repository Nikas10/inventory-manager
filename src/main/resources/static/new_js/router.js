const router = new VueRouter({
    routes: [
        {
            name: 'other',
            path: '*',
            redirect: '/',
        },
        {
            name: 'about',
            path: '/about',
            component: httpVueLoader('new_js/pages/about.vue'),
        },
        {
            name: 'equipment',
            path: '/equipment',
            component: httpVueLoader('new_js/pages/equipment.vue'),
        },
        {
            name: 'profile',
            path: '/profile',
            component: httpVueLoader('new_js/pages/profile.vue'),
        },
        {
            name: 'requests',
            path: '/requests',
            component: httpVueLoader('new_js/pages/requests.vue'),
        },
        {
          name: 'signin',
          path: '/signin',
          component: httpVueLoader('new_js/pages/signin.vue'),
        },
      {
        name: 'signup',
        path: '/signup',
        component: httpVueLoader('new_js/pages/signup.vue'),
      },
        {
            name: 'start',
            path: '/',
            component: httpVueLoader('new_js/pages/start.vue'),
        },
    ]
});
