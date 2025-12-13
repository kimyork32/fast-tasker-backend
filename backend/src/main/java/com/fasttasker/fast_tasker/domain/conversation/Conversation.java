@Entity
@Table(name = "conversation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "messages")
public class Conversation {

    @Id
    private UUID id;

    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Column(name = "participant_a_id", nullable = false)
    private UUID participantA;

    @Column(name = "participant_b_id", nullable = false)
    private UUID participantB;

    @OneToMany(
            mappedBy = "conversation",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Message> messages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

    public static Conversation open(UUID taskId, UUID participantA, UUID participantB) {
        if (taskId == null) {
            throw new IllegalArgumentException("TaskId cannot be null");
        }
        if (participantA == null || participantB == null) {
            throw new IllegalArgumentException("Participants cannot be null");
        }
        if (participantA.equals(participantB)) {
            throw new IllegalArgumentException("Participants must be different");
        }

        Conversation conversation = new Conversation();
        conversation.id = UUID.randomUUID();
        conversation.taskId = taskId;
        conversation.participantA = participantA;
        conversation.participantB = participantB;
        conversation.status = ConversationStatus.OPEN;

        return conversation;
    }

    public void sendMessage(UUID senderId, MessageContent content) {
        if (status == ConversationStatus.CLOSED) {
            throw new IllegalStateException("Cannot send messages in a closed conversation");
        }
        if (!isParticipant(senderId)) {
            throw new IllegalArgumentException("Sender is not a participant");
        }
        if (content == null) {
            throw new IllegalArgumentException("Message content cannot be null");
        }

        messages.add(Message.of(this, senderId, content));
    }

    public UUID otherParticipantId(UUID participantId) {
        if (participantA.equals(participantId)) return participantB;
        if (participantB.equals(participantId)) return participantA;
        throw new IllegalArgumentException("User is not a participant");
    }

    public List<Message> getMessages() {
        return List.copyOf(messages);
    }

    private boolean isParticipant(UUID userId) {
        return participantA.equals(userId) || participantB.equals(userId);
    }
}
