package org.acme.qute;

import io.quarkus.qute.Engine;
import io.quarkus.qute.EvalContext;
import io.quarkus.qute.Results;
import io.quarkus.qute.ValueResolver;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;

public class QuteTest {

	@Test
	public void test() {
		final ValueResolver valueResolver = context -> {
			if (context.getName().equals("name")) {
				final Item item = (Item) context.getBase();
				return CompletableFuture.completedFuture(item.name);
			}
			if (context.getName().equals("price")) {
				final Item item = (Item) context.getBase();
				return CompletableFuture.completedFuture(item.price);
			}
			return Results.NOT_FOUND;
		};
		final Engine engine = Engine.builder()
				.addDefaults()
				.addValueResolver(valueResolver)
				.build();
		final String content = engine.parse("{name} は {price} 円です")
				.data(new Item(BigDecimal.TEN, "スイカ"))
				.render();

		assertThat(content).isEqualTo("スイカ は 10 円です");
	}
}
