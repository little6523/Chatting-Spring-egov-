const common = {
	sendAjax: function(type, url, data, callback) {
		$.ajax({
			type: type,
			url: url,
			dataType: 'json',
			contentType: 'application/json',
			data: JSON.stringify(data),
			success: function(response, status, xhr) {
				if (response.rspCode == 1) {
					callback(response, xhr);
				}
			},
			error: function(request, status, error) {
				console.log(error);
			}
		})
	},

	nvl: function(data) {
		if (data == null) {
			return '-';
		}
		return data;
	}
}



