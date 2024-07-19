<template>
    <div>
        <h1>Task List</h1>
        <ul>
            <li v-for="task in tasks" :key="task.id">
                <h2>{{ task.title }}</h2>
                <p>{{ task.description }}</p>
                <p>Prazo: {{ new Date(task.deadLine).toLocaleDateString() }}</p>
                <button>Concluir</button>
                <button>Editar tarefa</button>
                <button>Excluir tarefa</button>
            </li>
        </ul>
        <button>Adicionar tarefa</button>
    </div>
</template>

<script>
export default {
    name: 'TaskList',
    data() {
        return {
            tasks: []
        }
    },
    async mounted() {
        await this.getTasks();
    },
    methods: {
        async getTasks() {
            try {
                const response = await fetch('https://todolist-leonardosf98-6f37cbc708fb.herokuapp.com/api/v1/tasks');
                const data = await response.json();
                this.tasks = data.map(task => {
                    return {
                        id: task.id,
                        title: task.title,
                        description: task.description,
                        deadLine: task.deadLine
                    }
                });
            } catch (error) {
                console.error('Error:', error);
            }
        }
    }
}
</script>
