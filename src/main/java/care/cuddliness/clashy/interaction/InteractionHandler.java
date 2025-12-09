package care.cuddliness.clashy.interaction;

import care.cuddliness.clashy.interaction.button.StacyButton;
import care.cuddliness.clashy.interaction.button.annotation.ClashyButtonComponent;
import care.cuddliness.clashy.interaction.button.data.ButtonExecutorInterface;
import care.cuddliness.clashy.command.ClashyCommandHandler;
import care.cuddliness.clashy.interaction.modal.ClashyModal;
import care.cuddliness.clashy.interaction.modal.annotation.ModalInput;
import care.cuddliness.clashy.interaction.modal.annotation.StacyModalComponent;
import care.cuddliness.clashy.interaction.modal.data.ModalExecutorInterface;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class InteractionHandler {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(ClashyCommandHandler.class);

    private static final @NotNull Map<String, ClashyModal> modalByName = new HashMap<>();
    private final @NotNull Map<String, StacyButton> buttonByName = new HashMap<>();

    public InteractionHandler(@NotNull ApplicationContext applicationContext){
        for (ButtonExecutorInterface button : applicationContext.getBeansOfType(ButtonExecutorInterface.class).values()) {
            ClashyButtonComponent buttonComponent = button.getClass().getAnnotation(ClashyButtonComponent.class);
            StacyButton buttonData = new StacyButton(button, buttonComponent.name());
            this.buttonByName.put(buttonComponent.name(), buttonData);
        }

        for (ModalExecutorInterface modal : applicationContext.getBeansOfType(ModalExecutorInterface.class).values()) {
            StacyModalComponent modalComponent = modal.getClass().getAnnotation(StacyModalComponent.class);
            ModalInput[] inputs = modal.getClass().getAnnotationsByType(ModalInput.class);
            ClashyModal modalData = new ClashyModal(modal, List.of(inputs), modalComponent.name());
            modalByName.put(modalComponent.name(), modalData);
        }
    }

    @EventListener(ModalInteractionEvent.class)
    public void onModalInteraction(ModalInteractionEvent event){
        CompletableFuture.runAsync(() -> {
            ModalExecutorInterface modalExecutor = this.determineModalExecutor(event.getModalId());
            if (modalExecutor == null) return;

            modalExecutor.onExecute(event.getMember(), event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling modal interaction event", throwable);
            return null;
        });

    }

    @EventListener(ButtonInteractionEvent.class)
    public void onButtonInteraction(ButtonInteractionEvent event){
        CompletableFuture.runAsync(() -> {
            ButtonExecutorInterface buttonExecutor = this.determineButtonExecutor(event.getComponentId());
            if (buttonExecutor == null) return;

            buttonExecutor.onExecute(event.getMember(), event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling button interaction event", throwable);
            return null;
        });

    }

    private @Nullable ButtonExecutorInterface determineButtonExecutor(String buttonName) {

        StacyButton button = buttonByName.get(buttonName);

        if(button == null){
            LOGGER.error("No button found with name: " + buttonName);
            return null;
        }else{
            return button.buttonExecutorInterface();
        }
    }


    public static Modal buildModal(String name){
        ClashyModal modal = modalByName.get(name);

        if(modal == null){
            LOGGER.error("No button found with name: " + name);
            return null;
        }else{
            Modal.Builder returnModal = Modal.create(name.toLowerCase(), name);
            List<Label> inputs = new ArrayList<>();
            for(ModalInput input : modal.options()){
                inputs.add(Label.of(input.name(), TextInput.create(input.name().toLowerCase(), input.inputstyle())
                        .setPlaceholder(input.placeholder()).
                        setMinLength(input.minLenght()).setMaxLength(input.maxLenght()).build()));
            }
            returnModal.addComponents(inputs);
            return returnModal.build();
        }
    }

    private @Nullable ModalExecutorInterface determineModalExecutor(String modalName) {

        ClashyModal modal = modalByName.get(modalName);

        if(modal == null){
            LOGGER.error("No button found with name: " + modalName);
            return null;
        }else{
            return modal.modalExecutorInterface();
        }
    }
}
